package com.orderingsystem.orderingsystem.repository.impl;

import com.orderingsystem.orderingsystem.dto.response.BillUserStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.TopProductStatsResponse;
import com.orderingsystem.orderingsystem.repository.StatsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class StatsRepositoryImpl implements StatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Double getRevenueFromBills(LocalDateTime start, LocalDateTime end) {
        String jpql = """
            SELECT SUM(b.total) FROM Bills b
            WHERE (:start IS NULL OR b.datecreate >= :start)
              AND (:end IS NULL OR b.datecreate <= :end)
        """;
        return entityManager.createQuery(jpql, Double.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public Double getRevenueFromReceipts(LocalDateTime start, LocalDateTime end) {
        String jpql = """
            SELECT SUM(r.total) FROM Receipts r
            WHERE (:start IS NULL OR r.datecreate >= :start)
              AND (:end IS NULL OR r.datecreate <= :end)
        """;
        return entityManager.createQuery(jpql, Double.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public List<TopProductStatsResponse> getTopSellingProducts(LocalDateTime start, LocalDateTime end, int limit) {
        String jpql = """
            SELECT new com.example.dto.stats.TopProductStatsResponse(
                p.id, p.name, SUM(d.quantity), 0.0
            )
            FROM BillDetails d
            JOIN d.bill b
            JOIN d.product p
            WHERE (:start IS NULL OR b.datecreate >= :start)
              AND (:end IS NULL OR b.datecreate <= :end)
            GROUP BY p.id, p.name
            ORDER BY SUM(d.quantity) DESC
        """;
        return entityManager.createQuery(jpql, TopProductStatsResponse.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<TopProductStatsResponse> getTopProfitableProducts(LocalDateTime start, LocalDateTime end, int limit) {
        String jpql = """
            SELECT new com.example.dto.stats.TopProductStatsResponse(
                p.id, p.name, SUM(d.quantity), SUM((p.salePrice - p.importPrice) * d.quantity)
            )
            FROM BillDetails d
            JOIN d.bill b
            JOIN d.product p
            WHERE (:start IS NULL OR b.datecreate >= :start)
              AND (:end IS NULL OR b.datecreate <= :end)
            GROUP BY p.id, p.name
            ORDER BY SUM((p.salePrice - p.importPrice) * d.quantity) DESC
        """;
        return entityManager.createQuery(jpql, TopProductStatsResponse.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Long countBills(LocalDateTime start, LocalDateTime end) {
        String jpql = """
            SELECT COUNT(b) FROM Bills b
            WHERE (:start IS NULL OR b.datecreate >= :start)
              AND (:end IS NULL OR b.datecreate <= :end)
        """;
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public Long countReceipts(LocalDateTime start, LocalDateTime end) {
        String jpql = """
            SELECT COUNT(r) FROM Receipts r
            WHERE (:start IS NULL OR r.datecreate >= :start)
              AND (:end IS NULL OR r.datecreate <= :end)
        """;
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public List<BillUserStatsResponse> getBillStatsByUser(LocalDateTime start, LocalDateTime end) {
        String jpql = """
            SELECT new com.example.dto.stats.BillUserStatsResponse(
                u.id, u.username, COUNT(b)
            )
            FROM Bills b
            JOIN b.user u
            WHERE (:start IS NULL OR b.datecreate >= :start)
              AND (:end IS NULL OR b.datecreate <= :end)
            GROUP BY u.id, u.username
            ORDER BY COUNT(b) DESC
        """;
        return entityManager.createQuery(jpql, BillUserStatsResponse.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
