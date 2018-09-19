package cn.edu.ahnu.wjcy.myapplication.Model.responBean;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/2 21:23，文件位于cn.edu.ahnu.wjcy.myapplication.Model
 * 文件作用：
 */

public class ScanResultSources {
    /*{
            "code": "SUCCESS",
                "data": {
            "attribute": {
                "itemBarcode": "6937451811449",
                        "itemId": "04b3c94cb05a46919f8a7bd63770113c",
                        "itemName": "不二家8支装牛奶棒糖（巧克力奶茶）",
                        "itemNum": 0,
                        "itemPrice": 5.2,
                        "itemPrintDate": "2017-09-29 10:24:42"
            }
        },
            "message": "成功"
        }*/

    private String code;
    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

        private AttributeBean attribute;

        public AttributeBean getAttribute() {
            return attribute;
        }

        public void setAttribute(AttributeBean attribute) {
            this.attribute = attribute;
        }

        public static class AttributeBean {
            /*"attribute": {
                "itemBarcode": "6937451811449",
                        "itemId": "04b3c94cb05a46919f8a7bd63770113c",
                        "itemName": "不二家8支装牛奶棒糖（巧克力奶茶）",
                        "itemNum": 0,
                        "itemPrice": 5.2,
                        "itemPrintDate": "2017-09-29 10:24:42"
            }*/

            private String itemBarcode;
            private String itemId;
            private float itemPrice;
            private String itemName;
            private int itemNum;
            private String itemPrintDate;
            private int itemWeight;
            private int itemSaleDiscount;
            private long itemSaleDiscountStart;
            private long itemSaleDiscountEnd;

            public String getItemBarcode() {
                return itemBarcode;
            }

            public void setItemBarcode(String itemBarcode) {
                this.itemBarcode = itemBarcode;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public float getItemPrice() {
                return itemPrice;
            }

            public void setItemPrice(float itemPrice) {
                this.itemPrice = itemPrice;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public int getItemNum() {
                return itemNum;
            }

            public void setItemNum(int itemNum) {
                this.itemNum = itemNum;
            }

            public String getItemPrintDate() {
                return itemPrintDate;
            }

            public void setItemPrintDate(String itemPrintDate) {
                this.itemPrintDate = itemPrintDate;
            }

            public int getItemWeight() {
                return itemWeight;
            }

            public void setItemWeight(int itemWeight) {
                this.itemWeight = itemWeight;
            }

            public int getItemSaleDiscount() {
                return itemSaleDiscount;
            }

            public void setItemSaleDiscount(int itemSaleDiscount) {
                this.itemSaleDiscount = itemSaleDiscount;
            }

            public long getItemSaleDiscountStart() {
                return itemSaleDiscountStart;
            }

            public void setItemSaleDiscountStart(long itemSaleDiscountStart) {
                this.itemSaleDiscountStart = itemSaleDiscountStart;
            }

            public long getItemSaleDiscountEnd() {
                return itemSaleDiscountEnd;
            }

            public void setItemSaleDiscountEnd(long itemSaleDiscountEnd) {
                this.itemSaleDiscountEnd = itemSaleDiscountEnd;
            }
        }
    }
}
