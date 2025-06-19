import SideBarItem from './SideBarItem';
import {  Calculator, FileText, Plus, Users } from 'lucide-react';
import Logo from './Logo'

function SideBar() {
    return (
        <div className='w-[350px] min-h-screen bg-white fixed'>
            <div className='p-6 border-b-[1px] border-gray-200'>
                <Logo />
            </div>
            <nav className='p-6'>
                <h2 className='uppercase mb-3 text-slate-400 font-semibold text-[12px] tracking-wider'>Menu Principal</h2>
                <div className='space-y-2'>
                    <SideBarItem icon={<Calculator size={20} />}>Criar Orçamento</SideBarItem>
                    <SideBarItem icon={<FileText size={20} />}>Visualizar Orçamentos</SideBarItem>
                    <SideBarItem icon={<Users size={20} />}>Visualizar Clientes</SideBarItem>
                    <SideBarItem icon={<Plus size={20} />}>Criar Cliente</SideBarItem>
                </div>
            </nav>
        </div>
    );
}

export default SideBar;