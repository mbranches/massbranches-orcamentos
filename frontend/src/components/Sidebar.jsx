import SidebarItem from './SidebarItem';
import {  Box, Calculator, FileText, Home, Plus, Users, X } from 'lucide-react';
import Logo from './Logo'
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { findMyBudgetQuantity } from '../services/budget';
import { findMyCustomerQuantity } from '../services/customer';
import { toast } from 'react-toastify';

function Sidebar({ sidebarOpen, setSidebarOpen, actualSection, refreshSidebar }) {
    const navigate = useNavigate();

    const [budgetCount, setBudgetCount] = useState(0); 

    const [customerCount, setCustomerCount] = useState(0); 

    const sidebarItems = [
        {
            label: "Tela Inicial",
            icon: <Home size={20} />,
            onClick: () => navigate("/home"),
            key: "tela-inicial"
        },
        {
            label: "Criar Orçamento",
            icon: <Calculator size={20} />,
            onClick: () => navigate("/orcamentos/criar"),
            key: "criar-orcamento"
        },
        {
            label: "Meus Orçamentos",
            icon: <FileText size={20} />,
            onClick: () => navigate("/orcamentos"),
            key: "visualizar-orcamentos"
        },
        {
            label: "Meus Clientes",
            icon: <Users size={20} />,
            onClick: () => navigate("/clientes"),
            key: "visualizar-clientes"
        },
        {
            label: "Meus Itens",
            icon: <Box size={20} />,
            onClick: () => navigate("/itens"),
            key: "visualizar-itens"
        }
    ]

    useEffect(() => {
        const fetchBudgetCount = async () => {
            const response = await findMyBudgetQuantity();

            setBudgetCount(response.data);
        };

        const fetchCustomerCount = async () => {
            const response = await findMyCustomerQuantity();

            setCustomerCount(response.data);
        };
    
        try {
            fetchBudgetCount();
            fetchCustomerCount();
        } catch (error) {
            const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
            statusValidate(status);
        }
    }, [refreshSidebar]);

    return (
        <>
            {sidebarOpen && (
                <div 
                    className="fixed inset-0 bg-gray-300 opacity-50 z-30 lg:hidden" 
                    onClick={() => setSidebarOpen(false)} 
                />
            )}

            <div className={`w-[310px] h-screen z-30 transition-transform duration-200 ease-in-out ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 fixed bg-white shadow-xl overflow-y-auto`}>
                <div className='p-6 border-b-[1px] border-gray-200 flex items-center gap-2'>
                    <div onClick={() => navigate("/home")}>
                        <Logo />
                    </div>
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
                            <div className='inline-flex px-2.5 py-0.5 bg-gray-100 rounded-[14px] font-semibold'>{budgetCount}</div>
                        </div>
                        <div className='flex items-center justify-between text-[14px]'>
                            <span className='text-gray-600'>Clientes Cadastrados</span>
                            <div className='inline-flex px-2.5 py-0.5 bg-gray-100 rounded-[14px] font-semibold'>{customerCount}</div>
                        </div>
                    </div>
                    
                </div>
            </div>
        </>
    );
}

export default Sidebar;