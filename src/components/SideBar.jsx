import SidebarItem from './SidebarItem';
import {  Calculator, FileText, Plus, Users } from 'lucide-react';
import Logo from './Logo'

function Sidebar() {
    return (
        <div className='w-[300px] min-h-screen hidden lg:block bg-white fixed'>
            <div className='p-6 border-b-[1px] border-gray-200'>
                <Logo />
            </div>
            <nav className='p-6'>
                <h2 className='uppercase mt-3 mb-3 text-slate-400 font-semibold text-[12px] tracking-wider'>Menu Principal</h2>
                <div className='space-y-2'>
                    <SidebarItem icon={<Calculator size={20} />}>Criar Orçamento</SidebarItem>
                    <SidebarItem icon={<FileText size={20} />}>Visualizar Orçamentos</SidebarItem>
                    <SidebarItem icon={<Users size={20} />}>Visualizar Clientes</SidebarItem>
                    <SidebarItem icon={<Plus size={20} />}>Criar Cliente</SidebarItem>
                </div>
            </nav>
            <div className='p-6'>
                <h2 className='uppercase mb-3 text-slate-400 font-semibold text-[12px] tracking-wider'>Estatísticas Rápidas</h2>
                <div className='space-y-3'>
                    <div className='flex items-center justify-between text-[14px]'>
                        <span className='text-gray-600'>Orçamentos Criados</span>
                        <div className='inline-flex px-2.5 py-0.5 bg-gray-100 rounded-[14px] font-semibold'>10</div>
                    </div>
                    <div className='flex items-center justify-between text-[14px]'>
                        <span className='text-gray-600'>Clientes Cadastrados</span>
                        <div className='inline-flex px-2.5 py-0.5 bg-gray-100 rounded-[14px] font-semibold'>10</div>
                    </div>
                </div>
                
            </div>
        </div>
    );
}

export default Sidebar;