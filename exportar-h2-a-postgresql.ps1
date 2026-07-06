$ErrorActionPreference = "Stop"

$java21 = "C:\Program Files\Android\openjdk\jdk-21.0.8\bin\java.exe"
$h2jar = "$env:USERPROFILE\.m2\repository\com\h2database\h2\2.4.240\h2-2.4.240.jar"
$workdir = "C:\Users\Franc\OneDrive\Escritorio\E"
$output = "$workdir\data\importar_en_render.sql"

# Column names en orden H2 (excluyendo ID) mapeados a PostgreSQL
$tables = @{
    "PACIENTES" = @{ cols = "direccion, email, fecha_nacimiento, nombre, residencia, rut, telefono"; seq = "pacientes_id_seq" }
    "PROFESIONALES" = @{ cols = "correo, especialidad, nombre, telefono"; seq = "profesionales_id_seq" }
    "CONSULTAS" = @{ cols = "fecha_consulta, ficha_paciente, ficha_profesional, modalidad, nombre_paciente, nombre_profesional, razon_consulta"; seq = "consultas_id_seq" }
    "FARMACIA" = @{ cols = "encargado_nombre, horario_farmacia, medicamentos, proveedor, stock_medicamentos, telefono_farmacia, telefono_proveedor"; seq = "farmacia_id_seq" }
    "PROGRAMAS" = @{ cols = "fecha_programa, lugar_programa, nombre_encargado, nombre_programa, tipo_programa"; seq = "programas_id_seq" }
    "RECETAS" = @{ cols = "fecha_emision, id_paciente, id_profesional, indicaciones_medicas, nombre_medicamentos, nombre_paciente, nombre_profesional"; seq = "recetas_id_seq" }
}

$header = @"
-- ============================================================
-- Script generado el $((Get-Date).ToString("yyyy-MM-dd HH:mm:ss"))
-- Exporta datos locales H2 -> PostgreSQL para Render
-- ============================================================

BEGIN;

"@
$header | Out-File -FilePath $output -Encoding utf8

$dbNames = @{
    "PACIENTES" = "pacientesdb"
    "PROFESIONALES" = "profesionalesdb"
    "CONSULTAS" = "consultasdb"
    "FARMACIA" = "farmaciadb"
    "PROGRAMAS" = "programasdb"
    "RECETAS" = "recetasdb"
}

foreach ($tn in $tables.Keys) {
    $dbName = $dbNames[$tn]
    $tmpFile = "$workdir\data\_tmp_$dbName.sql"
    Write-Host "Exportando $dbName ..."

    & $java21 -cp "$h2jar" org.h2.tools.Script -url "jdbc:h2:file:$workdir\data\$dbName" -user sa -script $tmpFile 2>$null

    $raw = Get-Content $tmpFile -Raw

    $tableInfo = $tables[$tn]
    $pat = "INSERT INTO " + '"PUBLIC"."' + $tn + '" VALUES'
    $idx = $raw.IndexOf($pat)
    if ($idx -ge 0) {
        $semi = $raw.IndexOf(";", $idx)
        if ($semi -ge 0) {
            $block = $raw.Substring($idx, $semi - $idx + 1)
            $block = $block.Replace('"PUBLIC".', "")
            $block = $block.Replace('"' + $tn + '"', $tn.ToLower())
            $block = $block.Replace('"', "")

            # Split multi-row VALUES into individual rows
            $lines = $block -split "`n"
            $tableLower = $tn.ToLower()
            $colList = $tableInfo.cols

            if ($lines[0].Trim() -match "^INSERT INTO $tableLower VALUES$") {
                Add-Content -Path $output -Value ("-- " + $tn + ": exportando datos")
                for ($i = 1; $i -lt $lines.Length; $i++) {
                    $val = $lines[$i].Trim().TrimEnd(",").TrimEnd(";")
                    if ($val -ne "" -and $val.StartsWith("(")) {
                        # Strip the ID (first numeric value) from the values
                        $rest = $val -replace '^\(\d+,\s*', '('
                        $sql = "INSERT INTO $tableLower ($colList) VALUES $rest"
                        if (-not $sql.EndsWith(";")) { $sql += ";" }
                        Add-Content -Path $output -Value $sql
                    }
                }
            }
            Add-Content -Path $output -Value ""

            # Max ID for sequence
            $maxId = 0
            foreach ($line in ($block -split "`n")) {
                $clean = $line.Trim().TrimEnd(",").TrimEnd(";")
                if ($clean -match '^\((\d+),') {
                    $id = [int]$Matches[1]
                    if ($id -gt $maxId) { $maxId = $id }
                }
            }
            if ($maxId -gt 0) {
                $nextId = $maxId + 1
                Add-Content -Path $output -Value ("ALTER SEQUENCE " + $tableInfo.seq + " RESTART WITH $nextId;")
                Add-Content -Path $output -Value ""
            }
        }
    } else {
        Add-Content -Path $output -Value ("-- " + $tn + ": sin datos")
        Add-Content -Path $output -Value ""
    }

    Remove-Item $tmpFile -Force
}

Add-Content -Path $output -Value "COMMIT;"
Add-Content -Path $output -Value "-- Fin del script de importacion"
Write-Host "`nArchivo generado: $output"
