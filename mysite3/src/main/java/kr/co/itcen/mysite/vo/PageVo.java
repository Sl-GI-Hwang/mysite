package kr.co.itcen.mysite.vo;

public class PageVo {
	private long boardCount;
	private long currentPage;
	private long pageCount;
	private long firstPage;
	public long getBoardCount() {
		return boardCount;
	}
	public void setBoardCount(long boardCount) {
		this.boardCount = boardCount;
	}
	public long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(long firstPage) {
		this.firstPage = firstPage;
	}
	@Override
	public String toString() {
		return "PageVo [boardCount=" + boardCount + ", currentPage=" + currentPage + ", pageCount=" + pageCount
				+ ", firstPage=" + firstPage + "]";
	}
}
