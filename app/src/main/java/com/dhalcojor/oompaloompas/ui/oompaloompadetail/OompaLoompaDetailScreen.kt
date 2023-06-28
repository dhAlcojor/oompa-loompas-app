package com.dhalcojor.oompaloompas.ui.oompaloompadetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaDetails
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaFavorite

@Composable
fun OompaLoompaDetailScreen(
    navController: NavHostController,
    viewModel: OompaLoompaDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoading && uiState.oompaLoompa == null) {
        viewModel.fetchOompaLoompaDetails(navController.currentBackStackEntry?.arguments?.getInt("oompaLoompaId"))
    }

    Scaffold(
        topBar = {
            OompaLoompaDetailTopBar(
                if (uiState.isLoading) "Oompa Loompa details" else "${uiState.oompaLoompa?.firstName} ${uiState.oompaLoompa?.lastName}",
                goBack = { navController.popBackStack() }
            )
        },
    ) { paddingValues ->
        OompaLoompaDetailContent(
            uiState.oompaLoompa,
            paddingValues
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OompaLoompaDetailTopBar(oompaLoompaName: String, goBack: () -> Unit) {
    TopAppBar(
        title = { Text(oompaLoompaName) },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
        }
    )
}

@Composable
private fun OompaLoompaDetailContent(
    oompaLoompa: OompaLoompaDetails?,
    padding: PaddingValues
) {
    if (oompaLoompa == null) {
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...")
        }
    } else {
        val selectedTabIndex = remember { mutableStateOf(0) }
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value
            ) {
                Tab(
                    text = { Text("Basic") },
                    selected = selectedTabIndex.value == 0,
                    onClick = { selectedTabIndex.value = 0 }
                )
                Tab(
                    text = { Text("Quota") },
                    selected = selectedTabIndex.value == 1,
                    onClick = { selectedTabIndex.value = 1 }
                )
                Tab(
                    text = { Text("Favorite") },
                    selected = selectedTabIndex.value == 2,
                    onClick = { selectedTabIndex.value = 2 }
                )
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                when (selectedTabIndex.value) {
                    0 -> BasicTabContent(oompaLoompa)
                    1 -> QuotaTabContent(oompaLoompa.quota)
                    2 -> FavoriteTabContent(oompaLoompa.favorite)
                }
            }
        }
    }
}

@Composable
private fun BasicTabContent(oompaLoompa: OompaLoompaDetails) {
    val fullName = "${oompaLoompa.firstName} ${oompaLoompa.lastName}"
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = oompaLoompa.image,
            placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
            contentDescription = "$fullName image",
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                fullName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(oompaLoompa.email, style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(Modifier.padding(vertical = 8.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(oompaLoompa.profession, style = MaterialTheme.typography.bodyLarge)
                Text(oompaLoompa.country, style = MaterialTheme.typography.bodyLarge)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Age: ${oompaLoompa.age}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Gender: ${oompaLoompa.gender}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Height: ${oompaLoompa.height}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                oompaLoompa.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun QuotaTabContent(quota: String) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Text(
            quota,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun FavoriteTabContent(favorite: OompaLoompaFavorite) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceBetween
        ) {
            Text(
                "Color: ${favorite.color}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                "Food: ${favorite.food}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            "Random:",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        Text(
            favorite.randomString,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            "Song",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Divider(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            favorite.song,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

private val previewOompaLoompa = OompaLoompaDetails(
    "Oompa",
    "Loompa",
    "https://placehold.co/600",
    "Developer",
    21,
    "F",
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligula. Fusce fermentum. Nullam cursus lacinia erat. Praesent blandit laoreet nibh. Fusce convallis metus id felis luctus adipiscing. Pellentesque egestas, neque sit amet convallis pulvinar, justo nulla eleifend augue, ac auctor orci leo non est. Quisque id mi. Ut tincidunt tincidunt erat. Etiam feugiat lorem non metus. Vestibulum dapibus nunc ac augue. Curabitur vestibulum aliquam leo. Praesent egestas neque eu enim. In hac habitasse platea dictumst. Fusce a quam. Etiam ut purus mattis mauris sodales aliquam. Curabitur nisi. Quisque malesuada placerat nisl. Nam ipsum risus, rutrum vitae, vestibulum eu, molestie vel, lacus. Sed augue ipsum, egestas nec, vestibulum et, malesuada adipiscing, dui. Vestibulum facilisis, purus nec pulvinar iaculis, ligula mi congue nunc, vitae euismod ligula urna in dolor. Mauris sollicitudin fermentum libero. Praesent nonummy mi in odio. Nunc interdum lacus sit amet orci. Vestibulum rutrum, mi nec elementum vehicula, eros quam gravida nisl, id fringilla neque ante vel mi. Morbi mollis tellus ac sapien. Phasellus volutpat, metus eget egestas mollis, lacus lacus blandit dui, id egestas quam mauris ut lacus. Fusce vel dui. Sed in libero ut nibh placerat accumsan. Proin faucibus arcu quis ante. In consectetuer turpis ut velit. Nulla sit amet est. Praesent metus tellus, elementum eu, semper a, adipiscing nec, purus. Cras risus ipsum, faucibus ut, ullamcorper id, varius ac, leo. Suspendisse feugiat. Suspendisse enim turpis, dictum sed, iaculis a, condimentum nec, nisi. Praesent nec nisl a purus blandit viverra. Praesent ac massa at ligula laoreet iaculis. Nulla neque dolor, sagittis eget, iaculis quis, molestie non, velit. Mauris turpis nunc, blandit et, volutpat molestie, porta ut, ligula. Fusce pharetra convallis urna. Quisque ut nisi. Donec mi odio, faucibus at, scelerisque quis, convallis in, nisi. Suspendisse non nisl sit amet velit hendrerit rutrum. Ut leo. Ut a nisl id ante tempus hendrerit. Proin pretium, leo ac pellentesque mollis, felis nunc ultrices eros, sed gravida augue augue mollis justo. Suspendisse eu ligula. Nulla facilisi. Donec id justo. Praesent porttitor, nulla vitae posuere iaculis, arcu nisl dignissim dolor, a pretium mi sem ut ipsum. Curabitur suscipit suscipit tellus. Praesent vestibulum dapibus nibh. Etiam iaculis nunc ac metus. Ut id nisl quis enim dignissim sagittis. Etiam sollicitudin, ipsum eu pulvinar rutrum, tellus ipsum laoreet sapien, quis venenatis ante odio sit amet eros. Proin magna. Duis vel nibh at velit scelerisque suscipit. Curabitur turpis. Vestibulum suscipit nulla quis orci. Fusce ac felis sit amet ligula pharetra condimentum. Maecenas egestas arcu quis ligula mattis placerat. Duis lobortis massa imperdiet quam. Suspendisse potenti. Pellentesque commodo eros a enim. Vestibulum turpis sem, aliquet eget, lobortis pellentesque, rutrum eu, nisl. Sed libero. Aliquam erat volutpat. Etiam vitae tortor. Morbi vestibulum volutpat enim. Aliquam eu nunc. Nunc sed turpis. Sed mollis, eros et ultrices tempus, mauris ipsum aliquam libero, non adipiscing dolor urna a orci. Nulla porta dolor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Pellentesque dapibus hendrerit tortor. Praesent egestas tristique nibh. Sed a libero. Cras varius. Donec vitae orci sed dolor rutrum auctor. Fusce egestas elit eget lorem. Suspendisse nisl elit, rhoncus eget, elementum ac, condimentum eget, diam. Nam at tortor in tellus interdum sagittis. Aliquam lobortis. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Curabitur blandit mollis lacus. Nam adipiscing. Vestibulum eu odio. Vivamus laoreet. Nullam tincidunt adipiscing enim. Phasellus tempus. Proin viverra, ligula sit amet ultrices semper, ligula arcu tristique sapien, a accumsan nisi mauris ac eros. Fusce neque. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante convallis tellus, vitae iaculis lacus elit id tortor. Vivamus aliquet elit ac nisl. Fusce fermentum odio nec arcu. Vivamus euismod mauris. In ut quam vitae odio lacinia tincidunt. Praesent ut ligula non mi varius sagittis. Cras sagittis. Praesent ac sem eget est egestas volutpat. Vivamus consectetuer hendrerit lacus. Cras non dolor. Vivamus in erat ut urna cursus vestibulum. Fusce commodo aliquam arcu. Nam commodo suscipit quam. Quisque id odio. Praesent venenatis metus at tortor pulvinar varius. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligula. Fusce fermentum. Nullam cursus lacinia erat. Praesent blandit laoreet nibh. Fusce convallis metus id felis luctus adipiscing. Pellentesque egestas, neque sit amet convallis pulvinar, justo nulla eleifend augue, ac auctor orci leo non est. Quisque id mi. Ut tincidunt tincidunt erat. Etiam feugiat lorem non metus. Vestibulum dapibus nunc ac augue. Curabitur vestibulum aliquam leo. Praesent egestas neque eu enim. In hac habitasse platea dictumst. Fusce a quam. Etiam ut purus mattis mauris sodales aliquam. Curabitur nisi. Quisque malesuada placerat nisl. Nam ipsum risus, rutrum vitae, vestibulum eu, molestie vel, lacus. Sed augue ipsum, egestas nec, vestibulum et, malesuada adipiscing, dui. Vestibulum facilisis, purus nec pulvinar iaculis, ligula mi congue nunc, vitae euismod ligula urna in dolor. Mauris sollicitudin fermentum libero. Praesent nonummy mi in odio. Nunc interdum lacus sit amet orci. Vestibulum rutrum, mi nec elementum vehicula, eros quam gravida nisl, id fringilla neque ante vel mi. Morbi mollis tellus ac sapien. Phasellus volutpat, metus eget egestas mollis, lacus lacus blandit dui, id egestas quam mauris ut lacus. Fusce vel dui. Sed in libero ut nibh placerat accumsan. Proin faucibus arcu quis ante. In consectetuer turpis ut velit. Nulla sit amet est. Praesent metus tellus, elementum eu, semper a, adipiscing nec, purus. Cras risus ipsum, faucibus ut, ullamcorper id, varius ac, leo. Suspendisse feugiat. Suspendisse enim turpis, dictum sed, iaculis a, condimentum nec, nisi. Praesent nec nisl a purus blandit viverra. Praesent ac massa at ligula laoreet iaculis. Nulla neque dolor, sagittis eget, iaculis quis, molestie non, velit. Mauris turpis nunc, blandit et, volutpat molestie, porta ut, ligula. Fusce pharetra convallis urna. Quisque ut nisi. Donec mi odio, faucibus at, scelerisque quis, convallis in, nisi. Suspendisse non nisl sit amet velit hendrerit rutrum. Ut leo. Ut a nisl id ante tempus hendrerit. Proin pretium, leo ac pellentesque mollis, felis nunc ultrices eros, sed gravida augue augue mollis justo. Suspendisse eu ligula. Nulla facilisi. Donec id justo. Praesent porttitor, nulla vitae posuere iaculis, arcu nisl dignissim dolor, a pretium mi sem ut ipsum. Curabitur suscipit suscipit tellus. Praesent vestibulum dapibus nibh. Etiam iaculis nunc ac metus. Ut id nisl quis enim dignissim sagittis. Etiam sollicitudin, ipsum eu pulvinar rutrum, tellus ipsum laoreet sapien, quis venenatis ante odio sit amet eros. Proin magna. Duis vel nibh at velit scelerisque suscipit. Curabitur turpis. Vestibulum suscipit nulla quis orci. Fusce ac felis sit amet ligula pharetra condimentum. Maecenas egestas arcu quis ligula mattis placerat. Duis lobortis massa imperdiet quam. Suspendisse potenti. Pellentesque commodo eros a enim. Vestibulum turpis sem, aliquet eget, lobortis pellentesque, rutrum eu, nisl. Sed libero. Aliquam erat volutpat. Etiam vitae tortor. Morbi vestibulum volutpat enim. Aliquam eu nunc. Nunc sed turpis. Sed mollis, eros et ultrices tempus, mauris ipsum aliquam libero, non adipiscing dolor urna a orci. Nulla porta dolor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Pellentesque dapibus hendrerit tortor. Praesent egestas tristique nibh. Sed a libero. Cras varius. Donec vitae orci sed dolor rutrum auctor. Fusce egestas elit eget lorem. Suspendisse nisl elit, rhoncus eget, elementum ac, condimentum eget, diam. Nam at tortor in tellus interdum sagittis. Aliquam lobortis. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Curabitur blandit mollis lacus. Nam adipiscing. Vestibulum eu odio. Vivamus laoreet. Nullam tincidunt adipiscing enim. Phasellus tempus. Proin viverra, ligula sit amet ultrices semper, ligula arcu tristique sapien, a accumsan nisi mauris ac eros. Fusce neque. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante convallis tellus, vitae iaculis lacus elit id tortor. Vivamus aliquet elit ac nisl. Fusce fermentum odio nec arcu. Vivamus euismod mauris. In ut quam vitae odio lacinia tincidunt. Praesent ut ligula non mi varius sagittis. Cras sagittis. Praesent ac sem eget est egestas volutpat. Vivamus consectetuer hendrerit lacus. Cras non dolor. Vivamus in erat ut urna cursus vestibulum. Fusce commodo aliquam arcu. Nam commodo suscipit quam. Quisque id odio. Praesent venenatis metus at tortor pulvinar varius. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligula. Fusce fermentum. Nullam cursus lacinia erat. Praesent blandit laoreet nibh. Fusce convallis metus id felis luctus adipiscing. Pellentesque egestas, neque sit amet convallis pulvinar, justo nulla eleifend augue, ac auctor orci leo non est. Quisque id mi. Ut tincidunt tincidunt erat. Etiam feugiat lorem non metus. Vestibulum dapibus nunc ac augue. Curabitur vestibulum aliquam leo. Praesent egestas neque eu enim. In hac habitasse platea dictumst. Fusce a quam. Etiam ut purus mattis mauris sodales aliquam. Curabitur nisi. Quisque malesuada placerat nisl. Nam ipsum risus, rutrum vitae, vestibulum eu, molestie vel, lacus. Sed augue ipsum, egestas nec, vestibulum et, malesuada adipiscing, dui. Vestibulum facilisis, purus nec pulvinar iaculis, ligula mi congue nunc, vitae euismod ligula urna in dolor. Mauris sollicitudin fermentum libero. Praesent nonummy mi in odio. Nunc interdum lacus sit amet orci. Vestibulum rutrum, mi nec elementum vehicula, eros quam gravida nisl, id fringilla neque ante vel mi. Morbi mollis tellus ac sapien. Phasellus volutpat, metus eget egestas mollis, lacus lacus blandit dui, id egestas quam mauris ut lacus. Fusce vel dui. Sed in libero ut nibh placerat accumsan. Proin faucibus arcu quis ante. In consectetuer turpis ut velit. Nulla sit amet est. Praesent metus tellus, elementum eu, semper a, adipiscing nec, purus. Cras risus ipsum, faucibus ut, ullamcorper id, varius ac, leo. Suspendisse feugiat. Suspendisse enim turpis, dictum sed, iaculis a, condimentum nec, nisi. Praesent nec nisl a purus blandit viverra. Praesent ac massa at ligula laoreet iaculis. Nulla neque dolor, sagittis eget, iaculis quis, molestie non, velit. Mauris turpis nunc, blandit et, volutpat molestie, porta ut, ligula. Fusce pharetra convallis urna. Quisque ut nisi. Donec mi odio, faucibus at, scelerisque quis, convallis in, nisi. Suspendisse non nisl sit amet velit hendrerit rutrum. Ut leo. Ut a nisl id ante tempus hendrerit. Proin pretium, leo ac pellentesque mollis, felis nunc ultrices eros, sed gravida augue augue mollis justo. Suspendisse eu ligula. Nulla facilisi. Donec id justo. Praesent porttitor, nulla vitae posuere iaculis, arcu nisl dignissim dolor, a pretium mi sem ut ipsum. Curabitur suscipit suscipit tellus. Praesent vestibulum dapibus nibh. Etiam iaculis nunc ac metus. Ut id nisl quis enim dignissim sagittis. Etiam sollicitudin, ipsum eu pulvinar rutrum, tellus ipsum laoreet sapien, quis venenatis ante odio sit amet eros. Proin magna. Duis vel nibh at velit scelerisque suscipit. Curabitur turpis. Vestibulum suscipit nulla quis orci. Fusce ac felis sit amet ligula pharetra condimentum. Maecenas egestas arcu quis ligula mattis placerat. Duis lobortis massa imperdiet quam. Suspendisse potenti. Pellentesque commodo eros a enim. Vestibulum turpis sem, aliquet eget, lobortis pellentesque, rutrum eu, nisl. Sed libero. Aliquam erat volutpat. Etiam vitae tortor. Morbi vestibulum volutpat enim. Aliquam eu nunc. Nunc sed turpis. Sed mollis, eros et ultrices tempus, mauris ipsum aliquam libero, non adipiscing dolor urna a orci. Nulla porta dolor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Pellentesque dapibus hendrerit tortor. Praesent egestas tristique nibh. Sed a libero. Cras varius. Donec vitae orci sed dolor rutrum auctor. Fusce egestas elit eget lorem. Suspendisse nisl elit, rhoncus eget, elementum ac, condimentum eget, diam. Nam at tortor in tellus interdum sagittis. Aliquam lobortis. Donec orci lectus, aliquam ut, faucibus non, euismod id, nulla. Curabitur blandit mollis lacus. Nam adipiscing. Vestibulum eu odio. Vivamus laoreet. Nullam tincidunt adipiscing enim. Phasellus tempus. Proin viverra, ligula sit amet ultrices semper, ligula arcu tristique sapien, a accumsan nisi mauris ac eros. Fusce neque. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante convallis tellus, vitae iaculis lacus elit id tortor. Vivamus aliquet elit ac nisl. Fusce fermentum odio nec arcu. Vivamus euismod mauris. In ut quam vitae odio lacinia tincidunt. Praesent ut ligula non mi varius sagittis. Cras sagittis. Praesent ac sem eget est egestas volutpat. Vivamus consectetuer hendrerit lacus. Cras non dolor. Vivamus in erat ut urna cursus vestibulum. Fusce commodo aliquam arcu. Nam commodo suscipit quam. Quisque id odio. Praesent venenatis metus at tortor pulvinar varius.Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligula. Fusce fermentum. Nullam cursus lacinia erat. Praesent blandit laoreet nibh. Fusce convallis metus id felis luctus adipiscing. Pellentesque egestas, neque sit amet convallis pulvinar, justo nulla eleifend augue, ac auctor orci leo non est. Quisque id mi. Ut tincidunt tincidunt erat. Etiam feugiat lorem non metus. Vestibulum dapibus nunc ac augue. Curabitur vestibulum aliquam leo. Praesent egestas neque eu enim. In hac habitasse platea dictumst. Fusce a quam. Etiam ut purus mattis mauris sodales aliquam. Curabitur nisi. Quisque malesuada placerat nisl. Nam ipsum risus, rutrum vitae, vestibulum eu, molestie vel, lacus. Sed augue ipsum, egestas nec, vestibulum et, malesuada adipiscing, dui. Vestibulum facilisis, purus nec pulvinar iaculis, ligula mi congue nunc, vitae euismod ligula urna in dolor. Mauris sollicitudin fermentum libero. Praesent nonummy mi in odio. Nunc interdum lacus sit amet orci. Vestibulum rutrum, mi nec elementum vehicula, eros quam gravida nisl, id fringilla neque ante vel mi. Morbi mollis tellus",
    75,
    "Loompalandia",
    "oompa@loompa.com",
    OompaLoompaFavorite(
        "red",
        "Chocolat",
        "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",
        "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do\n" +
                "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do\n" +
                "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do\n" +
                "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do\n" +
                "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do\n" +
                "Oompa Loompas:\n" +
                "Oompa Loompa doompadee doo\n" +
                "I've got another puzzle for you\n" +
                "Oompa Loompa doompadah dee\n" +
                "If you are wise you'll listen to me\n" +
                "What do you get from a glut of TV?\n" +
                "A pain in the neck and an IQ of three\n" +
                "Why don't you try simply reading a book?\n" +
                "Or could you just not bear to look?\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no\n" +
                "You'll get no commercials\n" +
                "Oompa Loompa Doompadee Dah\n" +
                "If you're like reading you will go far\n" +
                "You will live in happiness too\n" +
                "Like the Oompa\n" +
                "Oompa Loompa doompadee do"
    )
)

@Preview(showBackground = true)
@Composable
private fun OompaLoompaDetailsScreen() {
    Scaffold(
        topBar = {
            OompaLoompaDetailTopBar(
                "${previewOompaLoompa.firstName} ${previewOompaLoompa.lastName}",
                goBack = { }
            )
        },
    ) { paddingValues ->
        OompaLoompaDetailContent(
            previewOompaLoompa,
            paddingValues
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OompaLoompaTabRow() {
    OompaLoompaDetailContent(previewOompaLoompa, PaddingValues(0.dp))
}

@Preview(showBackground = true)
@Composable
private fun OompaLoompaFavorite() {
    FavoriteTabContent(favorite = previewOompaLoompa.favorite)
}