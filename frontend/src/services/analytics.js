import api from "./Api";

export async function getBudgetConversionRate() {
    return api("/budgets/analytics/conversion-rate");
}

export async function getBudgetConversionRateByCustomerType() {
  return api("/budgets/analytics/customers/type/conversion-rate");
}

export async function getTopCustomers() {
  return api("/budgets/analytics/customers/top");
}

export async function getTotalBudgeted() {
  return api("/budgets/analytics/total");
}

export async function getTotalApproved() {
  return api("/budgets/analytics/total-approved");
}