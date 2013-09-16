/*
 * 開発用master branch
 * 20130915-01 画像の埋め込み
 * 20130915-02 時間の表示（テキスト表示）
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
	        
	        Bitmap merokumako;
	        Bitmap merokuma;
	        
            int minute_rotate = 1;
            int second_rotate = 1;

	        private final Runnable mDraw = new Runnable() {
	            public void run() {
	                drawFrame();
	            }
	        };
	 
	        LiveWallpaperEngine() {
		        //壁紙画像
	        	merokumako  = BitmapFactory.decodeResource(getResources(), R.drawable.merokumako);
		        merokuma  = BitmapFactory.decodeResource(getResources(), R.drawable.merokuma);

		 
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
				
	            Canvas canvas = null;
	            canvas = holder.lockCanvas();
	            
	        	Matrix matrix = new Matrix();
	        	Matrix matrix2 = new Matrix();
	        	Paint paint = new Paint();
	        	
	        	//キャンバスを初期化灰色に染める
	        	canvas.drawColor(Color.argb(100, 225, 225, 225));
	        	
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

	            //0の処理
	            if(minute == 0){
	            	minute = 1;
	            }
	            if(second == 0){
	            	second = 1;
	            }
	            //フォントサイズ
				paint.setTextSize(30);
				//フォントカラー
				paint.setColor(Color.MAGENTA);
                                                                                                                              
	            /*
	            *
	            *   描画処理
	            *
	            */
	            //時計の針 int で取得の為、intで計算
	            minute_rotate = 360/minute;
	            second_rotate = 360/second;
	            
				//画面の中央を出す
				newWidth = newWidth/2-second;
				newHeight = newHeight/2-second;
	 								
				matrix.setTranslate(newWidth, newHeight);
				//回転にはpostがない。。。
				matrix.preRotate(second_rotate);
				matrix2.preRotate(minute_rotate);

	            try {
	            	//キャンバスがあったら、めろくまこちゃんを書き込む
	        	    if (canvas != null) {
	    				//イラスト書き込み
	        	    	canvas.drawBitmap(merokuma,matrix2,paint);
	        	    	canvas.drawBitmap(merokumako,matrix,paint);
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
	            if (visible) mHandler.postDelayed(mDraw, 500);
	 
	        }
	 
	    }
	}
