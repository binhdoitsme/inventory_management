package com.hanu.ims.controller;


import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.repository.AccountRepository;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.model.repository.OrderRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class OrderController {
    private OrderRepository orderRepository;
    private BatchRepository batchRepository;

    private static List<Batch> pendingBatches;

    public OrderController() {
        orderRepository = ServiceContainer.locateDependency(OrderRepository.class);
        batchRepository = ServiceContainer.locateDependency(BatchRepository.class);
        pendingBatches = new ArrayList<>();
    }

    /**
     * Assign a goods request to a map of batches with their respective quantities
     * <pre>
     * for each batch which are not yet out-of-stock
     *   if no more products to be taken
     *     break the loop
     *   if the batch has more products than needed
     *     update the batch
     *     update remainingQty
     *     break the loop
     *   else
     *     update the batch
     *     updateR remainingQty
     *     continue
     * </pre>
     * @param sku the requested Product SKU
     * @param quantity number of products requested
     * @return a list of tuple <Batch, quantity>
     */
    public Map<Batch, Integer> getBatches(String sku, int quantity) {
        List<Batch> batchesBySku = batchRepository.findAvailableBySku(sku);

        List<Batch> pendingBatchesWithSameSku =
                pendingBatches.stream()
                    .filter(b -> b.getSku().equals(sku))
                    .collect(Collectors.toList());
        if (!pendingBatchesWithSameSku.isEmpty() || batchesBySku.size() != pendingBatchesWithSameSku.size()) {
            for (Batch batch : pendingBatchesWithSameSku) {
                int batchId = batch.getId();
                int qty = batch.getQuantity();
                batchesBySku.stream().filter(b -> b.getId() == batchId).findFirst().get().setQuantity(qty);
            }
        }

        int remainingQty = quantity;
        Map<Batch, Integer> batchSelections = new HashMap<>();

        if (!batchesBySku.stream().anyMatch(b -> b.getQuantity() > 0)) {
            throw new NoSuchElementException("Out of stock!");
        }

        for (Batch batch : batchesBySku) { // batches are sorted from oldest to newest
            if (remainingQty <= 0) break;
            int currentBatchQty = batch.getQuantity();
            batchSelections.put(batch, remainingQty);
            if (currentBatchQty >= remainingQty) {
                int newBatchQty = currentBatchQty - remainingQty;
                batch.setQuantity(newBatchQty);
                currentBatchQty -= remainingQty;
                remainingQty = 0;
            } else {
                batch.setQuantity(0); // take all from the batch
                remainingQty -= currentBatchQty;
                currentBatchQty = 0;
            }
            if (pendingBatchesWithSameSku.isEmpty())
                pendingBatches.add(batch);
            else {
                pendingBatches.stream().filter(b -> b.getSku().equals(sku))
                        .findFirst().get().setQuantity(currentBatchQty);
            }
        }
        System.out.println(pendingBatches);
        return batchSelections;
    }

    /**
     * Add a created order to database and update related entities
     */
    public void createOrder(Order order) {
        if (order.getOrderLines().isEmpty()) {
            return;
        } else {
            try {
                orderRepository.beginTransaction();

                boolean isAddOrderSuccessful = orderRepository.add(order);

                int orderId = orderRepository.findAll().stream()
                        .filter(o -> o.getTimestamp() == order.getTimestamp())
                        .findFirst().get().getId();

                List<OrderLine> orderLinesToAdd = new ArrayList<>();
                orderLinesToAdd.addAll(order.getOrderLines());
                orderLinesToAdd.forEach(o -> {
                    o.setOrderId(orderId);
                });
                boolean isAddOrderLinesSuccessful = orderRepository.addOrderLines(order.getOrderLines());

                List<Batch> savedBatches = batchRepository.saveAll(pendingBatches);
                orderRepository.finishTransaction(false);
            } catch (DbException e) {
                e.printStackTrace();
                orderRepository.finishTransaction(true);
                throw new DbException(e);
            }


            // invalidating cache
            pendingBatches.clear();
        }
    }

    /**
     * Update an order and update related entities
     */
    public void updateOrder(Order order, List<OrderLine> removedLines, List<OrderLine> newLines) {
        // load related batches
        List<Batch> pendingUpdateBatches = new ArrayList<>();

        List<Batch> batchesToReturn = pendingUpdateBatchesFromOrderLines(removedLines, false);
        List<Batch> batchesToTake = pendingUpdateBatchesFromOrderLines(newLines, true);

        pendingUpdateBatches.addAll(batchesToReturn);
        pendingUpdateBatches.addAll(batchesToTake);

        orderRepository.save(order);
        orderRepository.removeOrderLines(removedLines);
        orderRepository.addOrderLines(newLines);
        batchRepository.saveAll(pendingUpdateBatches);
    }

    public void removeOrder(Order order) {
        List<OrderLine> orderLines = order.getOrderLines();
        List<Batch> pendingUpdateBatches = pendingUpdateBatchesFromOrderLines(orderLines, true);
        batchRepository.saveAll(pendingUpdateBatches);
        orderRepository.removeOrderLines(orderLines);
        orderRepository.delete(order);
    }

    private List<Batch> pendingUpdateBatchesFromOrderLines(List<OrderLine> orderLines, boolean isRemoved) {
        int coefficient = isRemoved ? -1 : 1;
        Map<Batch, Integer> relatedBatches = batchRepository.getBatchesAndQuantityFromOrderLines(orderLines);
        List<Batch> pendingUpdateBatches = new ArrayList<>();
        var keySet = relatedBatches.keySet();
        for (Batch batch : keySet) {
            int newQuantity = batch.getQuantity() + coefficient * relatedBatches.get(batch);
            batch.setQuantity(newQuantity);
            pendingUpdateBatches.add(batch);
        }
        return pendingUpdateBatches;
    }

    public ObservableList<Order> getOrderList() {
        ObservableList<Order> orderList = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Order> orders = orderRepository.findAll();
            orderList.setAll(orders);
        });
        dbThread.start();
        return orderList;
    }

    public ObservableValue<Order> getOrderById(int id) {
        SimpleObjectProperty<Order> orderToReturn = new SimpleObjectProperty<>();
        Thread dbThread = new Thread(() -> {
            Order orderFromDb = orderRepository.findById(id);
            if (orderFromDb == null) return;
            orderToReturn.set(orderFromDb);
        });
        dbThread.start();
        return orderToReturn;
    }

    public ObservableList<Product> getAllProductSuggestions() {
        ObservableList<Product> productObservableList = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Product> productList = orderRepository.getAllProductSuggestions();
            if (productList == null || productList.isEmpty()) {
                return;
            }
            productObservableList.setAll(productList);
        });
        dbThread.start();
        return productObservableList;
    }

    public void invalidateCache() {
        pendingBatches.clear();
    }
}