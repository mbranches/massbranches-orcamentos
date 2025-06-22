import { Calculator, FileText, Plus, Users } from "lucide-react";
import CardMenu from "./components/CardMenu";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import PanelLayout from "./components/PanelLayout";

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  
  const navigate = useNavigate();

  return (
    <div className="bg-slate-200">
      <PanelLayout sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} actualSection={"tela-inicial"} />

      <main className="min-h-screen mt-[-15px] flex justify-center items-center">

        <div className="lg:ml-[310px] p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6 max-w-6xl">
          <CardMenu icon={<Calculator size={24}/>} title="Criar Orçamento" description="Novo orçamento de obra" color="green" onButtonClick={() => navigate('/orcamentos/criar')}/>

          <CardMenu icon={<FileText size={24}/>} title="Visualizar Orçamentos" description="Gerenciar orçamentos existentes" color="blue"/>
          
          <CardMenu icon={<Users size={24}/>} title="Visualizar Clientes" description="Novo orçamento de obra" color="purple"/>
          
          <CardMenu icon={<Plus size={24}/>} title="Criar Cliente" description="Cadastrar novo cliente" color="orange"/>
        </div>
      </main>
    </div>
  );
}

export default App
