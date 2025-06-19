import { Calculator, FileText, MenuIcon, Plus, Users } from "lucide-react";
import CardMenu from "./components/CardMenu";
import Sidebar from './components/Sidebar'; 
import { useState } from "react";

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="bg-slate-200">
      <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

      <div className="lg:hidden fixed top-5 left-5">
        <button className="cursor-pointer" onClick={() => setSidebarOpen(!sidebarOpen)}>
          <MenuIcon />
        </button>
      </div>
      

      <main className="min-h-screen flex justify-center items-center">

        <div className="lg:ml-[310px] p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6 max-w-6xl">
          <CardMenu icon={<Calculator size={24}/>} title="Criar Orçamento" description="Novo orçamento de obra" color="green"/>

          <CardMenu icon={<FileText size={24}/>} title="Visualizar Orçamentos" description="Gerenciar orçamentos existentes" color="blue"/>
          
          <CardMenu icon={<Users size={24}/>} title="Visualizar Clientes" description="Novo orçamento de obra" color="purple"/>
          
          <CardMenu icon={<Plus size={24}/>} title="Criar Cliente" description="Cadastrar novo cliente" color="orange"/>
        </div>
      </main>
    </div>
  );
}

export default App
