package cn.edu.ahnu.wjcy.myapplication.Model.responBean;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/4 16:52，文件位于cn.edu.ahnu.wjcy.myapplication.Model.responBean
 * 文件作用：网络响应的状态信息
 */

public class PrecreateResponceNet {

    /**
     * data : {"payTypeCode":22,"orderId":"20171004160610490773445","qrCodeUrl":"https://qr.alipay.com/bax03252lhne1ry0nfp7803a"}
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
         * payTypeCode : 22
         * orderId : 20171004160610490773445
         * qrCodeUrl : https://qr.alipay.com/bax03252lhne1ry0nfp7803a
         */

        private int payTypeCode;
        private String orderId;
        private String qrCodeUrl;

        public int getPayTypeCode() {
            return payTypeCode;
        }

        public void setPayTypeCode(int payTypeCode) {
            this.payTypeCode = payTypeCode;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }
    }
}
