import SidebarItem from './SidebarItem';
import {  Calculator, FileText, Plus, Users } from 'lucide-react';
import Logo from './Logo'

function Sidebar() {
    return (
        <div className='w-[300px] min-h-screen bg-white fixed'>
            <div className='p-6 border-b-[1px] border-gray-200'>
                <Logo />
            </div>
            <nav className='p-6'>
                <h2 className='uppercase mb-3 text-slate-400 font-semibold text-[12px] tracking-wider'>Menu Principal</h2>
                <div className='space-y-2'>
                    <SidebarItem icon={<Calculator size={20} />}>Criar Orçamento</SidebarItem>
                    <SidebarItem icon={<FileText size={20} />}>Visualizar Orçamentos</SidebarItem>
                    <SidebarItem icon={<Users size={20} />}>Visualizar Clientes</SidebarItem>
                    <SidebarItem icon={<Plus size={20} />}>Criar Cliente</SidebarItem>
                </div>
            </nav>
        </div>
    );
}

export default Sidebar;