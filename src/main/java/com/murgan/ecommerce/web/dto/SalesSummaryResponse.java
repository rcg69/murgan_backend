package com.murgan.ecommerce.web.dto;

public class SalesSummaryResponse {
    private final long productsOrdered;
    private final long activeUsers;
    private final double totalRevenue;
    private final java.util.List<HistoryPoint> productsOrderedHistory;
    private final java.util.List<HistoryPoint> activeUsersHistory;
    private final java.util.List<HistoryPoint> totalRevenueHistory;

    public SalesSummaryResponse(long productsOrdered, long activeUsers, double totalRevenue,
                               java.util.List<HistoryPoint> productsOrderedHistory,
                               java.util.List<HistoryPoint> activeUsersHistory,
                               java.util.List<HistoryPoint> totalRevenueHistory) {
        this.productsOrdered = productsOrdered;
        this.activeUsers = activeUsers;
        this.totalRevenue = totalRevenue;
        this.productsOrderedHistory = productsOrderedHistory;
        this.activeUsersHistory = activeUsersHistory;
        this.totalRevenueHistory = totalRevenueHistory;
    }

    public long getProductsOrdered() { return productsOrdered; }
    public long getActiveUsers() { return activeUsers; }
    public double getTotalRevenue() { return totalRevenue; }
    public java.util.List<HistoryPoint> getProductsOrderedHistory() { return productsOrderedHistory; }
    public java.util.List<HistoryPoint> getActiveUsersHistory() { return activeUsersHistory; }
    public java.util.List<HistoryPoint> getTotalRevenueHistory() { return totalRevenueHistory; }

    public static class HistoryPoint {
        private final String name;
        private final long uv;

        public HistoryPoint(String name, long uv) {
            this.name = name;
            this.uv = uv;
        }

        public String getName() { return name; }
        public long getUv() { return uv; }
    }
}
