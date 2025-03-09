package com.sema.automauto.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sema.automauto.R
import com.sema.automauto.domain.util.FilterType
import com.sema.automauto.domain.util.SortingType
import com.sema.automauto.ui.screen.Tags
import com.sema.automauto.ui.theme.AutoMautoAppTheme
import com.sema.automauto.ui.theme.Shapes
import com.sema.automauto.ui.theme.Typography
import com.sema.component.LoadingBar
import com.sema.component.SearchTextField
import com.sema.component.ShowToast
import com.sema.data.model.CarSearchItem
import com.sema.shared_test.data.TestData
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CarSearchScreen(
    carsViewModel: CarsViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {
    val carsUiState = carsViewModel.carsUiState.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.cars_screen_title),
                style = Typography.h5,
                modifier = Modifier.padding(16.dp),
            )

            var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            var filter by remember { mutableStateOf<FilterType>(FilterType.Date(SortingType.Descending)) }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextField(
                    value = search, onValueChange = {
                        search = it
                        carsViewModel.searchCars(it.text)
                    }, hint = stringResource(R.string.cars_screen_search_hint),
                    color = MaterialTheme.colors.background
                )
                IconButton(onClick = {
                    showFilterDialog = true
                   // filter =
                        //filter.copy(if (filter.sortingType == SortingType.Descending) SortingType.Ascending else SortingType.Descending)
                   // carsViewModel.filterCars(selectedFilter)
                }, Modifier.testTag(Tags.FILTER)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null
                    )
                }
            }
            CarsContent(carsUiState, onImageClick = onClick)

            // Show Filter Dialog when button is clicked
            if (showFilterDialog) {
                FilterDialog(
                    filter = filter,
                    onDismiss = { showFilterDialog = false },
                    onApplyFilter = {selectedFilter->
                        filter = selectedFilter
                        carsViewModel.filterCars(filter)
                        showFilterDialog = false
                    }
                )
            }
        }
    }
}
@Composable
fun FilterDialog(
    filter: FilterType,
    onDismiss: () -> Unit,
    onApplyFilter: (FilterType) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(filter) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Filter Options", style = Typography.h6) },
        text = {
            Column {
                RadioButtonOption(
                    title = "Sort by Price",
                    isSelected = selectedFilter is FilterType.Price,
                    onClick = { selectedFilter = FilterType.Price(SortingType.Descending) }
                )
                RadioButtonOption(
                    title = "Sort by Date",
                    isSelected = selectedFilter is FilterType.Date,
                    onClick = { selectedFilter = FilterType.Date(SortingType.Descending) }
                )
                RadioButtonOption(
                    title = "Sort by Title",
                    isSelected = selectedFilter is FilterType.Title,
                    onClick = {
                        selectedFilter = if (selectedFilter is FilterType.Title &&
                            (selectedFilter as FilterType.Title).sortingType == SortingType.Ascending
                        ) {
                            FilterType.Title(SortingType.Descending)
                        } else {
                            FilterType.Title(SortingType.Ascending)
                        }
                    }
                )
                RadioButtonOption(
                    title = "Sort by Color",
                    isSelected = selectedFilter is FilterType.Color,
                    onClick = { selectedFilter = FilterType.Color(SortingType.Descending) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onApplyFilter(selectedFilter) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun RadioButtonOption(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(text = title, style = Typography.body1, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun CarsContent(carsUiState: State<CarsListUiState>, onImageClick: (String) -> Unit) {
    carsUiState.value.let { state ->
        when (state) {
            is CarsListUiState.Loading -> LoadingBar()
            is CarsListUiState.CarsListUiStateReady -> state.cars?.let {
                BindList(
                    it,
                    onImageClick = onImageClick
                )
            }

            is CarsListUiState.CarsListUiStateError -> state.error?.let { ShowToast(it) }
        }
    }
}

@Composable
fun CarImage(item: CarSearchItem) {
    item.images?.let {
        Image(
            painter = rememberAsyncImagePainter(/*it.first().url*/
            ImageRequest.Builder(LocalContext.current)
                .error(R.drawable.ic_filter)
                .placeholder(R.drawable.ic_filter)
                .data(it.first().url)
                .build()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
        )
    }
    if(item.images.isNullOrEmpty()){
        Image(
            painter = rememberAsyncImagePainter(R.drawable.ic_filter),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
        )
    }
}

@Composable
fun ListItem(item: CarSearchItem, onClick: (CarSearchItem) -> Unit) {
    Card(
        shape = Shapes.large,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable { onClick.invoke(item) }
            .testTag(Tags.CARS_LIST_ITEM + item.id),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            CarImage(item)
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "€${item.price}",
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle2,
                )
                Text(
                    text = "${item.make} ${item.model}",
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle1,
                )

                Text(
                    text = item.fuel,
                    style = Typography.caption,
                )
                Spacer(modifier = Modifier.height(8.dp))

                item.colour?.let {
                    Text(
                        text = stringResource(R.string.cars_screen_color, it),
                        style = Typography.subtitle2,
                    )
                }
                item.mileage?.let {
                    Text(
                        text = stringResource(R.string.cars_screen_miles, it),
                        style = Typography.subtitle2,
                    )
                }

                item.seller?.let {
                    Text(
                        text = stringResource(
                            R.string.cars_screen_miles,
                            "${it.type} ${it.phone} ${it.city}"
                        ),
                        style = Typography.caption,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.description,
                    style = Typography.caption,
                    maxLines = 2
                )

            }
        }
    }
}

@Composable
fun BindList(list: List<CarSearchItem>, onImageClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = Modifier.testTag(Tags.CARS_LIST_VIEW)
    ) {
        items(
            items = list,
            itemContent = {
                ListItem(it, onClick = { response ->
                    response.images?.first()?.url?.let { url ->
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        onImageClick.invoke(encodedUrl)
                    }
                })
            })
    }
}

@Preview
@Composable
fun CarSearchScreenPreview() {
    AutoMautoAppTheme {
        val data = CarsListUiState.CarsListUiStateReady(cars = TestData.mockCars)
        CarsContent(
            carsUiState = remember {
                mutableStateOf(data)
            },
            onImageClick = {})
    }
}