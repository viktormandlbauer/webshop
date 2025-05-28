package at.fhtw.webshop.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CustomerOrderDto {

    @NotNull
    private Integer shippingAddressId;

    @NotNull
    private Integer billingAddressId;

    @NotNull
    private Integer paymentMethodId;

    @NotNull
    private List<OrderItemDto> orderItemDtoList;

    public CustomerOrderDto() {
    }

    public CustomerOrderDto(Integer shippingAddressId, Integer billingAddressId, Integer paymentMethodId, List<OrderItemDto> orderItemDtoList) {
        this.shippingAddressId = shippingAddressId;
        this.billingAddressId = billingAddressId;
        this.paymentMethodId = paymentMethodId;
        this.orderItemDtoList = orderItemDtoList;
    }

    public Integer getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Integer shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public Integer getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Integer billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public List<OrderItemDto> getOrderItemDtoList() {
        return orderItemDtoList;
    }

    public void setOrderItemDtoList(List<OrderItemDto> orderItemDtoList) {
        this.orderItemDtoList = orderItemDtoList;
    }
}
