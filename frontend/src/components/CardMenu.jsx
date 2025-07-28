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

function CardMenu({title, description, icon, color, onButtonClick}) {
    return (
        <div className="bg-white rounded-md shadow-lg flex flex-col justify-between space-y-1.5">
            <div className="flex items-center w-full p-6 pb-4 space-x-4 mr-20">
                <div className={`w-[45px] h-[45px] p-3 ${bgMap[color]} flex items-center justify-center rounded-[14px] `}>
                    {icon}
                </div>

                <div>
                    <h2 className="text-[18px] font-semibold">{title}</h2>
                    <p className="text-[14px] text-slate-600">{description}</p>
                </div>
            </div>
            
            <div className="p-6 pt-0"> 
                <button className={`${buttonMap[color]} w-full px-8 py-2 rounded-md text-white font-semibold cursor-pointer text-[14px]`} onClick={onButtonClick}>Acessar</button>
            </div>
        </div>
    );
}

export default CardMenu;