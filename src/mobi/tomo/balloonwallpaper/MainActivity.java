package mobi.tomo.balloonwallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
				
				//画面の中央を出す
				newWidth = newWidth/2;
				newHeight = newHeight/2;
	 
	            Canvas canvas = null;
	            canvas = holder.lockCanvas();
	            
	        	Matrix matrix = new Matrix();
	        	Paint paint = new Paint();
	        	
	 
	            /*
	            *
	            *   描画処理
	            *
	            */
								
				matrix.setTranslate(newWidth, newHeight);

	            try {
	            	//キャンバスがあったら、めろくまこちゃんを書き込む
	        	    if (canvas != null) {
	        	    	canvas.drawBitmap(merokumako,matrix,paint);
	                }
	            }catch(ArithmeticException e){
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