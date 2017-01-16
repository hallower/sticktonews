package kr.blogspot.charlie0301.stickttonews.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomNestedScrollView extends android.support.v4.widget.NestedScrollView {

	private final long minTime = 70L;
	private final long maxTime = 170L;
	private final float minFlingLength = 300f;

	private OnHorizontalScrollListener horizontalScrollListener = null;

	private float totalYV;
	private float totalXV;

	private long startTime;
	private long elapsedTime;

	public CustomNestedScrollView(Context context) {
		super(context);
	}

	public CustomNestedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public interface OnHorizontalScrollListener {
		void onHorizontalScroll(NestedScrollView v, boolean isLeft);
	}

	public void setOnHorizontalScrollListener(OnHorizontalScrollListener l) {
		horizontalScrollListener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if(null == horizontalScrollListener)
			return super.onTouchEvent(ev);

		if(MotionEvent.ACTION_DOWN == ev.getAction())
		{
			totalYV = totalXV = 0;
			startTime = System.currentTimeMillis();
		}
		else
		{
			if(ev.getHistorySize() > 0){
				totalYV += ev.getY() - ev.getHistoricalY(0);
				totalXV += ev.getX() - ev.getHistoricalX(0);

				elapsedTime = System.currentTimeMillis() - startTime;
				//Log.d("csk", "touch : " + ", x = " + totalXV + ", y = " + totalYV + "( " + elapsedTime+  "ms )");

				if(Math.abs(totalXV) > Math.abs(totalYV) &&
						Math.abs(totalXV) > minFlingLength &&
						elapsedTime > minTime &&
						elapsedTime < maxTime) {

					Log.d("csk", "FIRE : " + ", x = " + totalXV + ", y = " + totalYV + "( " + elapsedTime+  "ms )");

					if(totalXV > 0) {
						horizontalScrollListener.onHorizontalScroll(this, true);
					} else {
						horizontalScrollListener.onHorizontalScroll(this, false);
					}
					totalXV = totalYV = 0;
					startTime = System.currentTimeMillis();
				}
			}
		}
		return super.onTouchEvent(ev);
	}

	// This always provides x = 0
	/*
	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		Log.d("csk", "fling : " + ", x = " + velocityX + ", y = " + velocityY);
		return super.dispatchNestedFling(velocityX, velocityY, consumed);
	}
	*/

	// This doesn't work, ACTION_MOVE is not fired.
	/*
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final float x = ev.getX();
		final float y = ev.getY();
		Log.d("csk", "mode : " + ev.getAction() + ", x = " + x + ", y = " + y);
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDistance = yDistance = 0f;
				lastX = ev.getX();
				lastY = ev.getY();

				// This is very important line that fixes
				computeScroll();

				break;
			case MotionEvent.ACTION_MOVE:
				final float curX = ev.getX();
				final float curY = ev.getY();
				xDistance += Math.abs(curX - lastX);
				yDistance += Math.abs(curY - lastY);
				lastX = curX;
				lastY = curY;

				if (xDistance > yDistance) {
					return false;
				}
		}
		return super.onInterceptTouchEvent(ev);
	}
	*/
}
