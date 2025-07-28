import { Calculator, FileText, Plus, Users } from "lucide-react";
import CardMenu from "./components/CardMenu";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import PanelLayout from "./components/PanelLayout";

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  
  const navigate = useNavigate();

  return (
    <div className="bg-gray-100">
      <PanelLayout sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} actualSection={"tela-inicial"}>
        <div className="lg:ml-[310px] mt-15 md:mt-0 p-6 grid grid-cols-1 md:grid-cols-3 gap-6 max-w-6xl ">
          <CardMenu icon={<Calculator size={24}/>} title="Criar Orçamento" description="Novo orçamento de obra" color="green" onButtonClick={() => navigate('/orcamentos/criar')}/>

          <CardMenu icon={<FileText size={24}/>} title="Visualizar Orçamentos" description="Gerenciar orçamentos existentes" color="blue" onButtonClick={() => navigate('/orcamentos')}/>
          
          <CardMenu icon={<Users size={24}/>} title="Visualizar Clientes" description="Novo orçamento de obra" color="purple" onButtonClick={() => navigate('/clientes')}/>
        </div>
      </PanelLayout>
    </div>
  );
}

export default App
