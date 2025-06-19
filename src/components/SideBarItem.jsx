function SideBarItem({icon, children}) {
    return (
        <button className="flex gap-3 text-slate-600 px-2 py-4 font-semibold text-[14px] cursor-pointer w-full hover:bg-gray-100 rounded-md">
            {icon}
            {children}
        </button>
    );
}

export default SideBarItem;