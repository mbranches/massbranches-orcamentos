function BudgetStatus({ status }) {
    const mappedColor = {
        "aprovado": "bg-green-100 text-green-800",
        "recusado": "bg-red-100 text-red-800",
        "em andamento": "bg-blue-100 text-blue-800",
        "em análise": "bg-yellow-100 text-yellow-800",
    };

    const formattedStatus = status ? status[0].toUpperCase() + status.slice(1).toLowerCase() : null;

    return (
        <span className={`inline-flex items-center px-3 py-1 text-sm font-semibold rounded-full ${mappedColor[status]}`}>
            {formattedStatus}
        </span>
    );
}

export default BudgetStatus;