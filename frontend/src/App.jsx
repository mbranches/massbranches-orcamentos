import { Box, Calculator, ChartNoAxesCombined, FileText, Users,  } from "lucide-react";
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
        <div className="lg:ml-[310px] mt-20 p-6 grid grid-cols-1 md:grid-cols-2 gap-12 max-w-6xl ">
          <CardMenu icon={<Calculator size={24}/>} title="Criar Orçamento" description="Novo orçamento de obra" color="green" onButtonClick={() => navigate('/orcamentos/criar')}/>

          <CardMenu icon={<FileText size={24}/>} title="Visualizar Orçamentos" description="Gerenciar meus orçamentos" color="blue" onButtonClick={() => navigate('/orcamentos')}/>
          
          <CardMenu icon={<Users size={24}/>} title="Meus Clientes" description="Gerenciar meus clientes" color="purple" onButtonClick={() => navigate('/clientes')}/>
          
          <CardMenu icon={<Box size={24}/>} title="Meus Itens" description="Gerenciar meus Itens" color="orange" onButtonClick={() => navigate('/clientes')}/>
          
          <div className="md:col-span-2">
            <CardMenu icon={<ChartNoAxesCombined size={24} />} title="Minhas Análises" description="Análises dos meus orçamentos" color="gray" onButtonClick={() => navigate('/analytics')}/>
          </div>
        </div>
      </PanelLayout>
    </div>
  );
}

export default App
