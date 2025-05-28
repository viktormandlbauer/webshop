package at.fhtw.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @NotNull
    @ColumnDefault("'Pending'")
    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "PaymentMethodID", nullable = false)
    private PaymentMethod paymentMethodID;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ShippingAddressID", nullable = false)
    private Address shippingAddressID;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "BillingAddressID", nullable = false)
    private Address billingAddressID;

    @Size(max = 255)
    @Column(name = "PdfFilePath")
    private String pdfFilePath;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedDate", updatable = false, nullable = false, insertable = false)
    private Instant createdDate;

    @NotNull
    @Column(name = "SumPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal sumPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(PaymentMethod paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public Address getShippingAddressID() {
        return shippingAddressID;
    }

    public void setShippingAddressID(Address shippingAddressID) {
        this.shippingAddressID = shippingAddressID;
    }

    public Address getBillingAddressID() {
        return billingAddressID;
    }

    public void setBillingAddressID(Address billingAddressID) {
        this.billingAddressID = billingAddressID;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

}