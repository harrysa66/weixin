package com.weixin.po.message;

import java.util.List;

/**
 * 图文消息
 * @author adwo1020
 *
 */
public class NewsMessage extends BaseMessage{

	private int ArticleCount;
	private List<News> Articles;
	
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
}
