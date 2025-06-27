import { Link } from 'react-router-dom';
import PanelLayout from '../components/PanelLayout'; // Importe seu layout
import { useState } from 'react';

function NotFoundPage() {
    const [sidebarOpen, setSidebarOpen] = useState();

    return (
        <div className="bg-slate-200">
            <PanelLayout sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen}> 
                <div className="text-center lg:ml-[310px]">
                    <h1 className="text-6xl font-bold text-slate-700">404</h1>
                    <p className="text-xl text-slate-600 mt-4">Página Não Encontrada</p>
                    <p className="text-slate-500 mt-2">
                        A página que você está procurando não existe ou foi movida.
                    </p>
                    <Link
                        to="/home"
                        className="mt-6 inline-block rounded bg-blue-600 px-5 py-3 text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring"
                    >
                        Voltar para o Início
                    </Link>
                </div>
            </PanelLayout>  
        </div>
    );
}

export default NotFoundPage;