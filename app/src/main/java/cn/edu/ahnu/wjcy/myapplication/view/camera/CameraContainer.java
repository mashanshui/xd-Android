package cn.edu.ahnu.wjcy.myapplication.view.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.edu.ahnu.wjcy.myapplication.R;
import cn.edu.ahnu.wjcy.myapplication.util.FileOperateUtil;


/**
 * @ClassName: CameraContainer
 * @Description:  相机界面的容器 包含相机绑定的surfaceview、拍照后的临时图片View和聚焦View
 * @author LinJ
 * @date 2014-12-31 上午9:38:52
 *
 */
public class CameraContainer extends RelativeLayout implements CameraOperation{

	public final static String TAG="CameraContainer";

	/** 相机绑定的SurfaceView  */
	private CameraView mCameraView;

	/** 触摸屏幕时显示的聚焦图案  */
	private FocusImageView mFocusImageView;

	/** 存放照片的根目录 */
	private String mSavePath;

	/** 照片字节流处理类  */
	private DataHandler mDataHandler;

	/** 拍照监听接口，用以在拍照开始和结束后执行相应操作  */
	private TakePictureListener mListener;

	/** 缩放级别拖动条 */
	private SeekBar mZoomSeekBar;

	/** 用以执行定时任务的Handler对象*/
	private Handler mHandler;
	private long mRecordStartTime;
	private SimpleDateFormat mTimeFormat;
	public CameraContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		mHandler=new Handler();
		mTimeFormat=new SimpleDateFormat("mm:ss",Locale.getDefault());
		setOnTouchListener(new TouchListener());
	}

	/**
	 *  初始化子控件
	 *  @param context
	 */
	private void initView(Context context) {
		inflate(context, R.layout.cameracontainer, this);
		mCameraView=(CameraView) findViewById(R.id.cameraView);

		mFocusImageView=(FocusImageView) findViewById(R.id.focusImageView);

		mZoomSeekBar=(SeekBar) findViewById(R.id.zoomSeekBar);
		//获取当前照相机支持的最大缩放级别，值小于0表示不支持缩放。当支持缩放时，加入拖动条。
		int maxZoom=mCameraView.getMaxZoom();
		if(maxZoom>0){
			mZoomSeekBar.setMax(maxZoom);
			mZoomSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		}
		mSavePath = context.getResources().getString(R.string.default_save_path);
		if(mDataHandler == null){
			mDataHandler = new DataHandler();
		}
	}


	@Override
	public boolean startRecord(){
		return false;
	}

	@Override
	public String stopRecord(){
		return "";
	}

	/**
	 *  改变相机模式 在拍照模式和录像模式间切换 两个模式的初始缩放级别不同
	 *  @param zoom   缩放级别
	 */
	public void switchMode(int zoom){
		mZoomSeekBar.setProgress(zoom);
		mCameraView.setZoom(zoom);
		//自动对焦
		mCameraView.onFocus(new Point(getWidth()/2, getHeight()/2), autoFocusCallback);
	}

	/**
	 *   前置、后置摄像头转换
	 */
	@Override
	public void switchCamera(){
		mCameraView.switchCamera();
	}
	/**
	 *  获取当前闪光灯类型
	 *  @return
	 */
	@Override
	public CameraView.FlashMode getFlashMode() {

		return mCameraView.getFlashMode();
	}

	/**
	 *  设置闪光灯类型
	 *  @param flashMode
	 */
	@Override
	public void setFlashMode(CameraView.FlashMode flashMode) {
		mCameraView.setFlashMode(flashMode);
	}


	/**
	 * 拍照方法
	 */
	public void takePicture(){
		takePicture(pictureCallback,mListener);
	}

	/**
	 * @Description: 拍照方法
	 * @param @param listener 拍照监听接口
	 * @return void
	 * @throws
	 */
	public void takePicture(TakePictureListener listener){
		this.mListener=listener;
		takePicture(pictureCallback, mListener);
	}


	@Override
	public void takePicture(PictureCallback callback,
							TakePictureListener listener) {
		mCameraView.takePicture(callback,listener);
	}

	@Override
	public int getMaxZoom() {
		// TODO Auto-generated method stub
		return mCameraView.getMaxZoom();
	}

	@Override
	public void setZoom(int zoom) {
		// TODO Auto-generated method stub
		mCameraView.setZoom(zoom);
	}

	@Override
	public int getZoom() {
		// TODO Auto-generated method stub
		return mCameraView.getZoom();
	}

	private final OnSeekBarChangeListener onSeekBarChangeListener=new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
									  boolean fromUser) {
			// TODO Auto-generated method stub
			mCameraView.setZoom(progress);
			mHandler.removeCallbacksAndMessages(mZoomSeekBar);
			//ZOOM模式下 在结束两秒后隐藏seekbar 设置token为mZoomSeekBar用以在连续点击时移除前一个定时任务
			mHandler.postAtTime(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mZoomSeekBar.setVisibility(View.GONE);
				}
			}, mZoomSeekBar,SystemClock.uptimeMillis()+2000);
		}



		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}



		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	};

	private final AutoFocusCallback autoFocusCallback=new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			//聚焦之后根据结果修改图片
			if (success) {
				mFocusImageView.onFocusSuccess();
			}else {
				//聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
				mFocusImageView.onFocusFailed();

			}
		}
	};

	private final PictureCallback pictureCallback=new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//重新打开预览图，进行下一次的拍照准备
			camera.startPreview();

			if(mSavePath==null){
				throw new RuntimeException("mSavePath is null");
			}
			if(mDataHandler==null){
				mDataHandler=new DataHandler();
			}
			mDataHandler.setMaxSize(100);
			Bitmap bm = mDataHandler.save(data);
			if(mListener!=null){
				mListener.onTakePictureEnd(bm);
			}
		}
	};

	private final class TouchListener implements OnTouchListener {

		/** 记录是拖拉照片模式还是放大缩小照片模式 */
		private static final int MODE_INIT = 0;
		/** 放大缩小照片模式 */
		private static final int MODE_ZOOM = 1;
		private int mode = MODE_INIT;// 初始状态

		/** 用于记录拖拉图片移动的坐标位置 */

		private float startDis;


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				// 手指压下屏幕
				case MotionEvent.ACTION_DOWN:
					mode = MODE_INIT;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					//如果mZoomSeekBar为null 表示该设备不支持缩放 直接跳过设置mode Move指令也无法执行
					if(mZoomSeekBar==null) return true;
					//移除token对象为mZoomSeekBar的延时任务
					mHandler.removeCallbacksAndMessages(mZoomSeekBar);
					mZoomSeekBar.setVisibility(View.VISIBLE);

					mode = MODE_ZOOM;
					/** 计算两个手指间的距离 */
					startDis = distance(event);
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == MODE_ZOOM) {
						//只有同时触屏两个点的时候才执行
						if(event.getPointerCount()<2) return true;
						float endDis = distance(event);// 结束距离
						//每变化10f zoom变1
						int scale=(int) ((endDis-startDis)/10f);
						if(scale>=1||scale<=-1){
							int zoom=mCameraView.getZoom()+scale;
							//zoom不能超出范围
							if(zoom>mCameraView.getMaxZoom()) zoom=mCameraView.getMaxZoom();
							if(zoom<0) zoom=0;
							mCameraView.setZoom(zoom);
							mZoomSeekBar.setProgress(zoom);
							//将最后一次的距离设为当前距离
							startDis=endDis;
						}
					}
					break;
				// 手指离开屏幕
				case MotionEvent.ACTION_UP:
					if(mode!=MODE_ZOOM){
						//设置聚焦
						Point point=new Point((int)event.getX(), (int)event.getY());
						mCameraView.onFocus(point,autoFocusCallback);
						mFocusImageView.startFocus(point);
					}else {
						//ZOOM模式下 在结束两秒后隐藏seekbar 设置token为mZoomSeekBar用以在连续点击时移除前一个定时任务
						mHandler.postAtTime(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mZoomSeekBar.setVisibility(View.GONE);
							}
						}, mZoomSeekBar,SystemClock.uptimeMillis()+2000);
					}
					break;
			}
			return true;
		}
		/** 计算两个手指间的距离 */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** 使用勾股定理返回两点之间的距离 */
			return (float) Math.sqrt(dx * dx + dy * dy);
		}

	}

	/**
	 * 拍照返回的byte数据处理类
	 * @author linj
	 *
	 */
	private final class DataHandler{
		/** 大图存放路径  */
		private String mThumbnailFolder;
		/** 小图存放路径 */
		private String mImageFolder;
		/** 压缩后的图片最大值 单位KB*/
		private int maxSize=100;

		public DataHandler(){
			mImageFolder= FileOperateUtil.getFolderPath(getContext(), FileOperateUtil.TYPE_IMAGE, mSavePath);
			mThumbnailFolder=FileOperateUtil.getFolderPath(getContext(),  FileOperateUtil.TYPE_THUMBNAIL, mSavePath);
			File folder=new File(mImageFolder);
			if(!folder.exists()){
				folder.mkdirs();
			}
			folder=new File(mThumbnailFolder);
			if(!folder.exists()){
				folder.mkdirs();
			}
		}

		/**
		 * 保存图片
		 *  相机返回的文件流
		 * @return 解析流生成的缩略图地址
		 */
		public Bitmap save(byte[] data){
			if(data!=null){
				//解析生成相机返回的图片
				Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
				byte[] compressData = compress(bm).toByteArray();
				return BitmapFactory.decodeByteArray(compressData, 0, compressData.length);
			}else{
				Toast.makeText(getContext(), "拍照失败，请重试", Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		/**
		 * 图片压缩方法
		 * @param bitmap 图片文件
		 * param max 文件大小最大值
		 * @return 压缩后的字节流
		 * @throws Exception
		 */
		public ByteArrayOutputStream compress(Bitmap bitmap){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//			int options = 99;
//			while ( baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//				options -= 10;// 每次都减少10
//				//压缩比小于0，不再压缩
//				if (options<0) {
//					break;
//				}
//				Log.e(TAG,baos.toByteArray().length / 1024+"");
//				baos.reset();// 重置baos即清空baos
//				bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//			}
			return baos;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}
	}

	/**
	 * @ClassName: TakePictureListener
	 * @Description:  拍照监听接口，用以在拍照开始和结束后执行相应操作
	 * @author LinJ
	 * @date 2014-12-31 上午9:50:33
	 *
	 */
	public static interface TakePictureListener{
		/**
		 *拍照结束执行的动作，该方法会在onPictureTaken函数执行后触发
		 *  @param bm 拍照生成的图片
		 */
		public void onTakePictureEnd(Bitmap bm);

		/**  临时图片动画结束后触发
		 * @param thumbPath 拍照生成的缩略图路径
		 * @param isVideo true：当前为录像缩略图 false:为拍照缩略图
		 * */
		public void onAnimtionEnd(String thumbPath, boolean isVideo);
	}


	/**
	 * dip转px
	 *  @param dipValue
	 *  @return
	 */
	private  int dip2px(float dipValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
}