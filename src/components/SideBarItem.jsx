function SidebarItem({icon, active, children}) {
    return (
        <button className={`flex gap-3 px-2 py-4 font-semibold text-[14px] cursor-pointer w-full rounded-md ${active ? "bg-blue-100 text-blue-700": "hover:bg-gray-100 text-slate-600"}`}>
            {icon}
            {children}
        </button>
    );
}

export default SidebarItem;