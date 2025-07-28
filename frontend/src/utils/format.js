export const formatCurrency = (value) => {

  return Number(!value ? 0 : value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
};

export const formatDate = (unformattedDate) => {
  const [year, month, day] = unformattedDate.split("-");
  
  return `${day}/${month}/${year}`
};

export const formatDecimal = (value) => {
  if (typeof value === "string") {
    return value.replace(",", ".");
  }
  return value;
};