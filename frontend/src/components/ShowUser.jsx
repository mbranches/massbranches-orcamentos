import { User } from "lucide-react";
import { useAuth } from "../hooks/useAuth";

function ShowUser() {
    const { user } = useAuth();

    return (
        <div className="flex items-center justify-center gap-3 cursor-pointer">
            <div className="w-[35px] h-[35px] bg-blue-700 flex items-center justify-center rounded-full">
                <User size={20} color="white"/>
            </div>
            <div>
                <span className="text-slate-600 text-[14px]">{user.firstName} {user.lastName}</span>
            </div>
        </div>
    );
}

export default ShowUser;