package com.kh.woofly.common;

public class Pagination {
	public static PageInfo getPageInfo(int currentPage, int listCount, int boardLimit) {
        int pageLimit = 9;   // 페이지 목록에 보여질 페이지 수
        int maxPage = (int) Math.ceil((double) listCount / boardLimit);
        int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
        int endPage = startPage + pageLimit - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        PageInfo pi = new PageInfo();
        pi.setCurrentPage(currentPage);
        pi.setListCount(listCount);
        pi.setPageLimit(pageLimit);
        pi.setMaxPage(maxPage);
        pi.setStartPage(startPage);
        pi.setEndPage(endPage);
        pi.setBoardLimit(boardLimit);

        return pi;
    }
}
	
