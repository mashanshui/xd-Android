package cn.edu.ahnu.wjcy.myapplication.Model;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/2 21:00，文件位于cn.edu.ahnu.wjcy.myapplication.Model
 * 文件作用：EventBus 基本事件
 */

public class EventBase {
    private String scanResult;

    public EventBase(String scanResult) {
        this.scanResult = scanResult;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public String toString() {
        return "EventBase{" +
                "scanResult='" + scanResult + '\'' +
                '}';
    }
}
