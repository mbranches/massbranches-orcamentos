function Action({icon, label, onClick}) {
    return (
        <div className='flex flex-col gap-2 items-center border-[0.5px] border-slate-300 py-2 h-16 px-4 font-semibold text-[12px] rounded-lg cursor-pointer hover:bg-blue-700 hover:text-white' onClick={onClick}>
            <div>
                {icon}
            </div>
            <div>
                {label}
            </div>
        </div>
    );
}

export default Action;