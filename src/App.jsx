import { Calculator, FileText, Plus, Users } from "lucide-react";
import CardMenu from "./components/CardMenu";
import Sidebar from "./components/Sidebar";

function App() {

  return (
    <div className="bg-slate-200">
      <Sidebar/>
      <div className="min-h-screen min-w-scree flex justify-center items-center">

        <div className="ml-[350px] max-w-[1000px] flex items-center justify-center gap-4 flex-wrap">
          <CardMenu icon={<Calculator size={24}/>} title="Criar Orçamento" description="Novo orçamento de obra" color="green"/>

          <CardMenu icon={<FileText size={24}/>} title="Visualizar Orçamentos" description="Gerenciar orçamentos existentes" color="blue"/>
          
          <CardMenu icon={<Users size={24}/>} title="Visualizar Clientes" description="Novo orçamento de obra" color="purple"/>
          
          <CardMenu icon={<Plus size={24}/>} title="Criar Cliente" description="Cadastrar novo cliente" color="orange"/>
        </div>
      </div>
    </div>
  );
}

export default App
