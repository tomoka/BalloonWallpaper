/*
 * 開発用master branch
 * 20130915-01 画像の埋め込み
 * 20130915-02 時間の表示（テキスト表示）
 * 20130918	   時計素材実装
 * */





package mobi.tomo.balloonwallpaper;

import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class MainActivity extends WallpaperService {
	    private final Handler mHandler = new Handler();
	    	 
	    @Override
	    public void onCreate() {
	        super.onCreate();
	    }
	 
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }
	 
	    @Override
	    public Engine onCreateEngine() {
	    	return new LiveWallpaperEngine();
	    }
	 
	    class LiveWallpaperEngine extends Engine {
	 
	        private boolean visible;
	        
	        Bitmap clock_hour;
	        Bitmap clock_minute;
	        Bitmap clock_second;
	        Bitmap clock_center;
	        Bitmap clock_background;
	        
            int minute_rotate = 1;
            int second_rotate = 1;
            int hour_rotate = 1;
            
            int clock_backgroundWidth;
            int clock_backgroundHeight;
            float clock_centerWidth;
            float clock_centerHeight;

	        private final Runnable mDraw = new Runnable() {
	            public void run() {
	                drawFrame();
	            }
	        };
	 
	        LiveWallpaperEngine() {
		        //壁紙画像
	        	clock_hour  = BitmapFactory.decodeResource(getResources(), R.drawable.clockhour);
	        	clock_minute  = BitmapFactory.decodeResource(getResources(), R.drawable.clockminute);
	        	clock_second  = BitmapFactory.decodeResource(getResources(), R.drawable.clocksecond);
	        	clock_center  = BitmapFactory.decodeResource(getResources(), R.drawable.clockcenter);
	        	clock_background  = BitmapFactory.decodeResource(getResources(), R.drawable.clockbackground);
	        	clock_backgroundWidth = clock_background.getWidth();
	        	clock_backgroundHeight = clock_background.getHeight();
	        	clock_centerWidth = clock_center.getWidth();
	        	clock_centerHeight = clock_center.getHeight();

		 
	        }
	 
	        @Override
	        public void onCreate(SurfaceHolder surfaceHolder) {
	            super.onCreate(surfaceHolder);
	        }
	 
	        @Override
	        public void onDestroy() {
	            super.onDestroy();
	            mHandler.removeCallbacks(mDraw);
	        }
	 
	        @Override
	        public void onVisibilityChanged(boolean visible) {
	            this.visible = visible;
	            if (visible) {
	                drawFrame();
	            } else {
	                mHandler.removeCallbacks(mDraw);
	            }
	        }
	 
	        @Override
	        public void onSurfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            super.onSurfaceChanged(holder, format, width, height);
	            drawFrame();
	        }
	 
	        @Override
	        public void onSurfaceCreated(SurfaceHolder holder) {
	            super.onSurfaceCreated(holder);
	        }
	 
	        @Override
	        public void onSurfaceDestroyed(SurfaceHolder holder) {
	            super.onSurfaceDestroyed(holder);
	            mHandler.removeCallbacks(mDraw);
	        }
	 
	        @Override
	        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
	        	Log.d("tag",
						String.format(
								"onOffsetsChanged:{xOffset:%f, yOffset:%f, xStep:%f, yStep:%f, xPixels:%d, yPixels:%d}",
								xOffset, yOffset,xStep,yStep,xPixels,yPixels));
	        	drawFrame();
	        }
	 
	        @Override
	        public void onTouchEvent(MotionEvent event) {
	            super.onTouchEvent(event);
	        }
	 
	        void drawFrame() {
	            final SurfaceHolder holder = getSurfaceHolder();
	            
			    /*
			     * 端末の大きさを取得
			     */
				// ウィンドウマネージャのインスタンス取得
				WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
				// ディスプレイのインスタンス生成
				Display disp = wm.getDefaultDisplay();
				
				float newWidth = disp.getWidth();
				float newHeight = disp.getHeight();
				
				float percent = newWidth/clock_backgroundWidth;
				float windowWidth = newWidth/2;
				float windowHeight = newHeight/2;
				
    	    	float clock_minuteWidthsize = clock_minute.getWidth()*percent/2;
    	    	float clock_secondWidthsize = clock_second.getWidth()*percent/2;
    	    	float clock_hourWidthsize = clock_hour.getWidth()*percent/2;
    	    	
	        	clock_centerWidth = clock_centerWidth*percent/2;
	        	clock_centerHeight = clock_centerHeight*percent/2;

	            Canvas canvas = null;
	            canvas = holder.lockCanvas();
	            
	        	Matrix matrix_second = new Matrix();
	        	Matrix matrix_minute = new Matrix();
	        	Matrix matrix_hour = new Matrix();
	        	Matrix matrix_center = new Matrix();
	        	Matrix matrix_background = new Matrix();
	        	Paint paint = new Paint();
	        	
	        	//キャンバスを初期化灰色に染める
	        	canvas.drawColor(Color.argb(255, 255, 255, 255));
	        	
                /*
                 * 時計表示のための数値取得
                 */
	        	//時間取得クラス
	            Calendar calendar = Calendar.getInstance();
	            
	            int year = calendar.get(Calendar.YEAR);
	            int month = calendar.get(Calendar.MONTH);
	            int day = calendar.get(Calendar.DATE);
	            int hour = calendar.get(Calendar.HOUR_OF_DAY);
	            int minute = calendar.get(Calendar.MINUTE);
	            int second = calendar.get(Calendar.SECOND);
	            String clock_count = year + "年" + month + "月" + day + "日" + hour + "時" + minute + "分" + second + "秒";                                                                                           

	            //フォントサイズ
				paint.setTextSize(30);
				//フォントカラー
				paint.setColor(Color.GRAY);
                                                                                                                              
	            /*
	            *
	            *   描画処理
	            *
	            */
	            //時計の針 int で取得の為、intで計算
	            minute_rotate = 180+6*minute;
	            second_rotate = 180+6*second;
	            hour_rotate = 180+6*hour;
	            	 								
				matrix_background.setScale(percent, percent);
				matrix_center.setScale(percent, percent);

				//回転にはpostがない。。。
				matrix_second.setScale(percent, percent);
				matrix_second.preRotate(second_rotate);

				matrix_minute.setScale(percent, percent);
				matrix_minute.preRotate(minute_rotate);
				
				matrix_hour.setScale(percent, percent);
				matrix_hour.preRotate(hour_rotate);
				
				//背景の中心の計算
				clock_backgroundHeight = (int) (clock_backgroundHeight*percent);
				clock_backgroundHeight = (int) (windowHeight/2-clock_backgroundHeight/2);
								
				matrix_center.postTranslate(windowWidth-clock_centerWidth, windowHeight-clock_centerHeight);
				matrix_background.postTranslate(0, clock_backgroundHeight);
				
				matrix_second.postTranslate(windowWidth+clock_secondWidthsize,windowHeight);
				matrix_minute.postTranslate(windowWidth+clock_minuteWidthsize,windowHeight);
				matrix_hour.postTranslate(windowWidth+clock_hourWidthsize,windowHeight);
				
	            try {
	            	//キャンバスがあったら、めろくまこちゃんを書き込む
	        	    if (canvas != null) {
	    				//イラスト書き込み
	        	    	canvas.drawBitmap(clock_background,matrix_background,paint);
	        	    	canvas.drawBitmap(clock_second,matrix_second,paint);
	        	    	canvas.drawBitmap(clock_minute,matrix_minute,paint);
	        	    	canvas.drawBitmap(clock_hour,matrix_hour,paint);
	        	    	canvas.drawBitmap(clock_center,matrix_center,paint);
	    				//時計書き込み
	    	            canvas.drawText(clock_count, 0, 100, paint);
	                }
	            }catch(ArithmeticException e){
                    //例外処理があった場合、ログを出す
	            	String msg = "ArithmeticException e";
	            	String tag = "ArithmeticException e";
	            	Log.v(tag, msg);
	            } finally {
	            	//書き込みを終了させる
	                if (canvas != null) holder.unlockCanvasAndPost(canvas);
	            }
	            
	 
	            mHandler.removeCallbacks(mDraw);
	            if (visible) mHandler.postDelayed(mDraw, 25);
	 
	        }
	 
	    }
	}
