package cn.edu.ahnu.wjcy.myapplication.Model.responBean;

/**
 * Created by Cuibiming on 2017-10-09.
 */

public class CloseOrderResult {
    /**
     * data : {"totalAmount":0.01,"payTypeCode":21,"payOrderId":"","orderId":"20160818164813221850281","buyerEmail":"","payStatus":-1}
     * statusCode : 000
     * statusMsg : 成功
     */

    private DataBean data;
    private String statusCode;
    private String statusMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public static class DataBean {
        /**
         * totalAmount : 0.01
         * payTypeCode : 21
         * payOrderId :
         * orderId : 20160818164813221850281
         * buyerEmail :
         * payStatus : -1
         */

        private double totalAmount;
        private int payTypeCode;
        private String payOrderId;
        private String orderId;
        private String buyerEmail;
        private int payStatus;

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getPayTypeCode() {
            return payTypeCode;
        }

        public void setPayTypeCode(int payTypeCode) {
            this.payTypeCode = payTypeCode;
        }

        public String getPayOrderId() {
            return payOrderId;
        }

        public void setPayOrderId(String payOrderId) {
            this.payOrderId = payOrderId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }
    }
}
