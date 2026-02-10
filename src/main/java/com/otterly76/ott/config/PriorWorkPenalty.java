package com.otterly76.ott.config;


import java.util.function.IntUnaryOperator;

public enum PriorWorkPenalty {
    NONE((itemRepairCost) -> 0),
    VANILLA(IntUnaryOperator.identity()),
    LIMITED((itemRepairCost) -> limitedRepairCost(repairCostToRepairs(itemRepairCost)));

    public final IntUnaryOperator operator;

    PriorWorkPenalty(IntUnaryOperator operator) {
        this.operator = operator;
    }

    static int repairCostToRepairs(int itemRepairCost) {
        ++itemRepairCost;

        int priorRepairs;
        for(priorRepairs = 0; itemRepairCost >= 2; ++priorRepairs) {
            itemRepairCost /= 2;
        }

        return priorRepairs;
    }

    static int limitedRepairCost(int priorRepairs) {
        int itemRepairCost = 0;

        for(int i = 0; i < priorRepairs; ++i) {
            itemRepairCost += Math.min(itemRepairCost + 1, OttConfig.ANVILS.PRIOR_WORK_PENALTY.MAXIMUM_PRIOR_WORK_PENALTY_INCREASE.get());
        }

        return itemRepairCost;
    }
}