import { Search } from "lucide-react";

function SearchBar({ onSearch, placeholder }) {
    return(
        <div className='flex items-center border border-gray-300 rounded-md p-2 w-full'>
            <div className='flex items-center justify-center px-2'>
                <Search size={15} />
            </div>

                <input type="text" placeholder={placeholder} className='block text-sm w-full px-1 py-1 outline-none' onChange={ onSearch }/>
        </div>
    );
}

export default SearchBar;