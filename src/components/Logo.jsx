import { Building2 } from "lucide-react";

function Logo() {
    return (
        <div className='relative flex items-center gap-2'>
            <div className="h-10 w-10 rounded-lg bg-blue-700 flex justify-center items-center">
                <Building2 size={24} color='white'/>
            </div>
            
            <div className='ml-1'>
                <h1 className='text-[20px] font-bold flex flex-col'>
                    Orçamentos
                </h1>
                <p className='text-slate-500'> Gestão de Orçamentos</p>
            </div>
        </div>
    );
}

export default Logo;