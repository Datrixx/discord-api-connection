import { importx } from "@discordx/importer";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

export async function loadCommands() {
    try { 
        await importx(path.resolve(__dirname, "../commands/**/*.{ts,js}"));
        console.log("✅ Comandos cargados correctamente");
    } catch (error) {
        console.error("❌ Error al cargar los comandos:", error);
    }
    
}
