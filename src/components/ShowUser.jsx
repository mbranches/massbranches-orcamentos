import { User } from "lucide-react";

function ShowUser() {
    return (
        <div className="flex items-center justify-center gap-3 cursor-pointer">
            <div className="w-[35px] h-[35px] bg-blue-700 flex items-center justify-center rounded-full">
            <User size={20} color="white"/>
            </div>
            <div>
            <span className="text-slate-600 text-[14px]">Marcelo Viana Branches</span>
            </div>
        </div>
    );
}

export default ShowUser;