import { MenuIcon } from "lucide-react";
import ShowUser from "./ShowUser";
import Sidebar from "./Sidebar";

function PanelLayout({sidebarOpen, setSidebarOpen, actualSection, children}) {
    return (
        <div>
            <div>
                <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} actualSection={actualSection}/>

                <header className="w-full fixed flex top-0 left-0 justify-between p-6 bg-white shadow-sm shadow-slate-200">
                    <div className="lg:hidden">
                        <button className="cursor-pointer" onClick={() => setSidebarOpen(!sidebarOpen)}>
                            <MenuIcon />
                        </button>
                    </div>

                    <div className="flex-1"></div>

                    <ShowUser />
                </header>
            </div>
            <main className="min-h-screen flex justify-center items-center">
                {children}
            </main>
        </div>
    );
}

export default PanelLayout;