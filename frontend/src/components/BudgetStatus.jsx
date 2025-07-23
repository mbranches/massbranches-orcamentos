function BudgetStatus({ status }) {
    const mappedColor = {
        "aprovado": "bg-green-100 text-green-800",
        "recusado": "bg-red-100 text-red-800",
        "em andamento": "bg-blue-100 text-blue-800",
        "em análise": "bg-yellow-100 text-yellow-800",
    };

    const formattedStatus = status[0].toUpperCase() + status.slice(1).toLowerCase();

    return (
        <span className={`inline-flex items-center px-2 py-1 text-xs font-semibold rounded-full ${mappedColor[status]}`}>
            {formattedStatus}
        </span>
    );
}

export default BudgetStatus;