import { Building2 } from "lucide-react";

function Logo() {
    return (
        <div className='relative flex gap-2'>
            <div className="p-3 rounded-lg bg-blue-600">
                <Building2 size={32} color='white'/>
            </div>
            
            <div className='ml-2'>
                <h1 className='text-[20px] font-bold flex flex-col'>
                    Orçamentos
                </h1>
                <p className='text-slate-400'> Gestão de Orçamentos</p>
            </div>
        </div>
    );
}

export default Logo;