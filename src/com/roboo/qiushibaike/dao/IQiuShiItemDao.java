package com.roboo.qiushibaike.dao;

import java.util.LinkedList;

import com.roboo.qiushibaike.model.QiuShiItem;

 

public interface IQiuShiItemDao
{
	/***
	 * 将数据保存到数据库中
	 * @param item:要保存的数据
	 * @return
	 */
	public int insert(QiuShiItem item);
	/**
	 * 判断当前要插入数据是否已经存在于数据库中
	 * @param md5:一条糗事对应的唯一的md5码
	 * @return
	 */
	public boolean isInserted(String md5);
	/**
	 * 获取插入数据库中的数据根据类型
	 * @param type:要获取的数据的类型
	 * @return
	 */
	public LinkedList<QiuShiItem> getItems(String type);
	/***
	 * 获取插入数据库中的指定页码的数据根据数据累心
	 * @param type:要获取的数据的类型
	 * @param pageNo:当前要获取的页码
	 * @return
	 */
	public LinkedList<QiuShiItem> getItems(String type,int pageNo);
	/***
	 * 用于下拉刷新，当下拉刷新成功获取数据后删除以前的相同类型的数据，保存最新的下拉刷新数据到数据库
	 * @param type:要获取的数据的类型
	 * @return
	 */
	public boolean removePreviousItems(String type);
}
