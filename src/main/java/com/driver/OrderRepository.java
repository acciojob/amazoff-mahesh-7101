package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    private HashMap<String,Order> orderMap;
    private HashMap<String,DeliveryPartner>deliverypartnerMap;
    private HashMap<String, List<String>>orderpartnerpair;

    public OrderRepository() {
        this.orderMap = new HashMap<String, Order>();
        this.deliverypartnerMap = new HashMap<String, DeliveryPartner>();
        this.orderpartnerpair = new  HashMap<String, List<String>>();
    }

    public HashMap<String, Order> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(HashMap<String, Order> orderMap) {
        this.orderMap = orderMap;
    }

    public HashMap<String, DeliveryPartner> getDeliverypartnerMap() {
        return deliverypartnerMap;
    }

    public void setDeliverypartnerMap(HashMap<String, DeliveryPartner> deliverypartnerMap) {
        this.deliverypartnerMap = deliverypartnerMap;
    }

    public HashMap<String, List<String>> getOrderpartnerpair() {
        return orderpartnerpair;
    }

    public void setOrderpartnerpair(HashMap<String, List<String>> orderpartnerpair) {
        this.orderpartnerpair = orderpartnerpair;
    }
    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }
    public void addPartner(String partnerId){
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        deliverypartnerMap.put(partnerId,partner);
    }
    public void addOrderPartnerPair(String orderId, String partnerId){
        List<String> ordersList = new ArrayList<>();
         if(orderMap.containsKey(orderId)&&deliverypartnerMap.containsKey(partnerId)) {
            if (orderpartnerpair.containsKey(partnerId)) {
                ordersList = orderpartnerpair.get(partnerId);
                ordersList.add(orderId);
                orderpartnerpair.put(partnerId, ordersList);
                int no = deliverypartnerMap.get(partnerId).getNumberOfOrders();
                deliverypartnerMap.get(partnerId).setNumberOfOrders(no + 1);
            } else {
                ordersList.add(orderId);
                orderpartnerpair.put(partnerId, ordersList);
                int no = deliverypartnerMap.get(partnerId).getNumberOfOrders();
                deliverypartnerMap.get(partnerId).setNumberOfOrders(no + 1);
            }
        }
    }
    public Order getOrderById(String orderId){
        if(orderMap.containsKey(orderId))
            return orderMap.get(orderId);
        else
            return null;
    }
    public DeliveryPartner getPartnerById(String partnerId){
        if(deliverypartnerMap.containsKey(partnerId))
            return deliverypartnerMap.get(partnerId);
        else
            return null;
    }
    public int getOrderCountByPartnerId(String partnerId){
        return deliverypartnerMap.get(partnerId).getNumberOfOrders();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        if(orderpartnerpair.containsKey(partnerId))
            return orderpartnerpair.get(partnerId);
        else
            return null;
    }
    public List<String> getAllOrders(){
        List<String>orders=new ArrayList();
        for(String oId:orderMap.keySet())
            orders.add(oId);
        return orders;
    }
    public int getCountOfUnassignedOrders(){
        int c=0;
        for(String pId:deliverypartnerMap.keySet())
            c+=deliverypartnerMap.get(pId).getNumberOfOrders();
        return orderMap.size()-c;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int c=0;
        int t=Integer.valueOf(time.substring(0,2))*60+Integer.valueOf(time.substring(3,5));
        for(String oId:orderpartnerpair.get(partnerId)){
            if(t>=orderMap.get(oId).getDeliveryTime())
                c++;
        }
        return deliverypartnerMap.get(partnerId).getNumberOfOrders()-c;
    }
    public String getLastDeliveryTimeByPartnerId( String partnerId){
        List<Integer>lt=new ArrayList<>();
        for(String oId:orderpartnerpair.get(partnerId)){
            lt.add(orderMap.get(oId).getDeliveryTime());
        }
        Collections.sort(lt);

        int t=lt.get(lt.size()-1);
        return String.valueOf(t/60)+":"+String.valueOf(t%60);
    }
    public void deletePartnerById(String partnerId){

        orderpartnerpair.remove(partnerId);
        deliverypartnerMap.remove(partnerId);
    }
    public void deleteOrderById(String orderId){

        for(String pId:orderpartnerpair.keySet()){
            List<String>orders=orderpartnerpair.get(pId);
            for(String order:orders){
                if(order.equals(orderId)){
                    orders.remove(String.valueOf(orderId));
                    orderpartnerpair.put(pId,orders);
                    orderMap.remove(orderId);
                }

            }
        }
    }

}
