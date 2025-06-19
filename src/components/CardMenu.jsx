const bgMap = {
    green: "bg-emerald-100 text-emerald-600",
    blue: "bg-blue-100 text-blue-600",
    purple: "bg-purple-100 text-purple-600",
    orange: "bg-orange-100 text-orange-600",
  };

  const buttonMap = {
    green: "bg-emerald-500 hover:bg-emerald-600",
    blue: "bg-blue-500 hover:bg-blue-600",
    purple: "bg-purple-500 hover:bg-purple-600",
    orange: "bg-orange-500 hover:bg-orange-600",
  };

function CardMenu({title, description, icon, color}) {
    return (
        <div className="bg-white rounded-md shadow-lg p-6 flex flex-col">
            <div className="relative">
                <div className={`w-[45px] ${bgMap[color]} h-[45px] absolute left-0 top-0 bottom-0 flex items-center justify-center rounded-[14px] `}>
                    {icon}
                </div>
                <div className="ml-15">
                    <h2 className="text-[18px] font-semibold">{title}</h2>
                    <p className="text-[14px] text-slate-600">{description}</p>
                </div>
            </div>
            <button className={`${buttonMap[color]} mt-4 w-[350px] px-4 py-2 rounded-md text-white font-semibold cursor-pointer text-[14px]`}>Acessar</button>
        </div>
    );
}

export default CardMenu;