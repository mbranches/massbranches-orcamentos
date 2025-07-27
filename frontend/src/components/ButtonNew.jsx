import { Plus } from "lucide-react";

function ButtonNew({ onClick, label }) {
    return (
        <div
            className='bg-black text-white flex items-center px-4 py-2 rounded-sm h-10 gap-2 cursor-pointer hover:bg-gray-800 transition-all duration-100' 
            onClick={onClick}
        >
            <div><Plus size={20} /></div>
            <span className='text-sm'>{label}</span>
        </div>
    )
}

export default ButtonNew;