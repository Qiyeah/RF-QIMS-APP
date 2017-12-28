package com.zhilian.hzrf_oa.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.zhilian.hzrf_oa.R;

/**
 * 暂时不用
 * 自定义的listview，实现下拉加载更多数据（listview的分页功能）
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {
	View footer;// 底部布局
	int totalItemCount;// 定义item的总数量
	int lastItemCount;// 记录最后一个item的位置
	boolean isLoading;// 正在加载

	ILoadListener iLoadListener;// 定义的接口回调

	public LoadListView(Context context) {
		super(context);
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	/**
	 * 添加底部加载提示布局到listview
	 * @param context
	 */
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	/**
	 * 滑动事件的监听
	 * @param view
	 * @param firstVisibleItem 第一个可以见的item
	 * @param visibleItemCount 可以见的item数量
	 * @param totalItemCount 总的数量
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		this.totalItemCount = totalItemCount;
		// 通过成员变量记录下最后一个item的索引位置
		this.lastItemCount = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 判断如果当前的条目是最后一个条目,并且滚动状态是空闲状态,那么证明已经滚动到最底部
		if (totalItemCount == lastItemCount
				&&scrollState==SCROLL_STATE_IDLE){
			if (!isLoading){
				isLoading = true;
				footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
				// 使用接口回调，加载更多数据，让外部实现这个接口,然后回调onLoad方法
				iLoadListener.onLoad();
			}
		}
	}

	// 加载完毕，隐藏footer
	public void laodComplete() {
		if (isLoading){
			isLoading = false;
			footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		}
	}

	public void setInterface(ILoadListener iLoadListener){
		this.iLoadListener = iLoadListener;
	}

	// 加载更多数据的回调接口
	public interface ILoadListener{
		public void onLoad();
	}

}