function CustomerType({ type }) {
    const mappedColor = {
        "PESSOA_FISICA": "bg-blue-100 text-blue-800 border border-blue-300",
        "PESSOA_JURIDICA": "bg-green-100 text-green-800 border border-green-300",
    };

    const mappedType = {
        "PESSOA_FISICA": "Pessoa Física", 
        "PESSOA_JURIDICA": "Pessoa Jurídica", 
    }

    return (
        <span className={`inline-flex items-center px-2 py-[2px] text-xs font-semibold rounded-full ${mappedColor[type]}`}>
            {mappedType[type]}
        </span>
    );
}

export default CustomerType;