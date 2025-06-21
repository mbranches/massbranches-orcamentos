import SidebarItem from './SidebarItem';
import {  Calculator, FileText, Home, Plus, Users, X } from 'lucide-react';
import Logo from './Logo'

function Sidebar({ sidebarOpen, setSidebarOpen, actualSection }) {
    const sidebarItems = [
        {
            label: "Tela Inicial",
            icon: <Home size={20} />,
            key: "tela-inicial"
        },
        {
            label: "Criar Orçamento",
            icon: <Calculator size={20} />,
            key: "criar-orcamento"
        },
        {
            label: "Visualizar Orçamentos",
            icon: <FileText size={20} />,
            key: "visualizar-orcamentos"
        },
        {
            label: "Visualizar Clientes",
            icon: <Users size={20} />,
            key: "visualizar-clientes"
        },
        {
            label: "Criar Cliente",
            icon: <Plus size={20} />,
            key: "criar-cliente"
        },
    ]

    return (
        <>
            {sidebarOpen && (
                <div 
                    className="fixed inset-0 bg-gray-300 opacity-50 z-40 lg:hidden" 
                    onClick={() => setSidebarOpen(false)} 
                />
            )}

            <div className={`w-[310px] h-screen z-50 transition-transform duration-100 ease-in-out ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 fixed bg-white shadow-xl overflow-y-auto`}>
                <div className='p-6 border-b-[1px] border-gray-200 flex items-center gap-2'>
                    <Logo />
                    <div className='hover:bg-gray-100 p-1 rounded-md lg:hidden cursor-pointer' onClick={() => setSidebarOpen(false)}>
                        <X size={20} />
                    </div>
                </div>
                <nav className='p-6'>
                    <h2 className='uppercase mt-3 mb-3 text-slate-400 font-semibold text-[12px] tracking-wider'>Menu Principal</h2>
                    <div className='space-y-2'>
                        {sidebarItems.map(item => 
                            <SidebarItem icon={item.icon} key={item.key} active={item.key === actualSection} onClick={item.onClick}>
                                {item.label}
                            </SidebarItem>
                        )}
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
        </>
    );
}

export default Sidebar;