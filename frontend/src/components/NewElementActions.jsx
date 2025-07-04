import { Check, X } from "lucide-react";

function NewElementActions({ handlerSave, handlerRemove }) {
    return (
        <div className='flex gap-1'>
            <div 
                className='bg-green-600 flex justify-center items-center p-2 rounded-lg text-white font-normal cursor-pointer'
                onClick={handlerSave}
            >
                <Check size={18} />
            </div>

            <div
                className='flex justify-center items-center p-2 rounded-lg text-slate-600 font-normal border border-slate-300 cursor-pointer'
                onClick={handlerRemove}
            >
                <X size={18}/>
            </div>
        </div>
    );
}

export default NewElementActions;