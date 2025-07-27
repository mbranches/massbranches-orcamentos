function CardAction({icon, onClick, children, color = "default"}) {
    const colorMap = {
        default: "",
        red: "text-red-600",
    };

    return (
        <div 
            className={`${colorMap[color]} text-sm flex gap-1 cursor-pointer items-center border border-slate-300 hover:border-slate-400 px-3 py-1 rounded-md transition-all duration-200`} 
            onClick={onClick}
        >
            {icon}
            <span>{children}</span>
        </div>
    );
}

export default CardAction;