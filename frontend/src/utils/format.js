export const formatCurrency = (value) => {

  return Number(!value ? 0 : value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
};

export const formatDecimal = (value) => {
  if (typeof value === "string") {
    return value.replace(",", ".");
  }
  return value;
};